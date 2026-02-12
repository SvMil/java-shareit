package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.BookingMapper;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.dto.BookingShortDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.NotOwnerException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.comment.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemForOwnerDto;
import ru.practicum.shareit.item.dto.ItemUpdateDto;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final ItemMapper itemMapper;
    private final BookingMapper bookingMapper;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;
    private final BookingRepository bookingRepository;


    @Override
    public ItemDto getItemById(Long itemId) {
        Item item = itemRepository.findByIdWithComments(itemId)
                .orElseThrow(() -> new NotFoundException("Предмет не найден"));

        return itemMapper.toDto(item);
    }


    private Item findItemById(Long itemId) {
        return itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Вещь не найдена"));
    }

    @Override
    public List<ItemDto> getItemsByUserId(Long userId) {
        checkUserExists(userId);
        return itemMapper.toDto(itemRepository.findByOwnerId(userId));
    }


    private User findUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден"));
    }


    @Override
    public List<ItemForOwnerDto> getItemsByOwner(Long ownerId) {
        findUserById(ownerId);

        List<Item> allUserItems = itemRepository.findByOwnerId(ownerId).stream()
                .sorted(Comparator.comparing(Item::getId))
                .collect(Collectors.toList());

        Map<Long, List<Booking>> bookingMap = bookingRepository.findAllByItemInAndStatusOrderByStartAsc(allUserItems, BookingStatus.APPROVED)
                .stream()
                .collect(groupingBy(booking -> booking.getItem().getId(), toList()));

        Map<Long, List<Comment>> commentMap = commentRepository.findAllByItemIn(allUserItems).stream()
                .collect(groupingBy(commentDto -> commentDto.getItem().getId(), toList()));


        return allUserItems.stream()
                .map(it -> ItemMapper.toItemReturnDto(it,
                        findLastBooking(bookingMap.get(it.getId())),
                        findNextBooking(bookingMap.get(it.getId())),
                        mapperListComment(commentMap.get(it.getId()))))
                .collect(toList());
    }

    private List<CommentDto> mapperListComment(List<Comment> comments) {
        if (comments == null || comments.isEmpty()) {
            return new ArrayList<>();
        }
        return comments.stream()
                .map(CommentMapper::toCommentReturnDto)
                .collect(toList());
    }

    private BookingShortDto findLastBooking(List<Booking> bookings) {
        if (bookings == null || bookings.isEmpty()) {
            return null;
        }

        return bookings.stream()
                .filter(bookingDto -> !bookingDto.getStart().isAfter(LocalDateTime.now()))
                .reduce((b1, b2) -> b1.getStart().isAfter(b2.getStart()) ? b1 : b2)
                .map(bookingMapper::toBookingShortDto)
                .orElse(null);
    }

    private BookingShortDto findNextBooking(List<Booking> booking) {
        if (booking == null || booking.isEmpty()) {
            return null;
        }

        return booking.stream()
                .filter(bookingDto -> bookingDto.getStart().isAfter(LocalDateTime.now()))
                .findFirst()
                .map(bookingMapper::toBookingShortDto)
                .orElse(null);
    }

    @Override
    public List<ItemDto> searchItems(String text) {
        if (text.isBlank()) {
            return Collections.emptyList();
        }
        return itemMapper.toDto(itemRepository.search(text));
    }

    @Override
    @Transactional
    public ItemDto addItem(ItemDto itemDto, Long userId) {
        User owner = findUserById(userId);

        Item item = itemMapper.toEntity(itemDto);
        item.setOwner(owner);
        return itemMapper.toDto(itemRepository.save(item));
    }

    @Override
    @Transactional
    public ItemDto updateItem(Long itemId, ItemUpdateDto itemDto, Long userId) {

        Item item = findItemById(itemId);
        checkOwner(item, userId);

        if (itemDto.getName() != null && !itemDto.getName().isBlank()) {
            item.setName(itemDto.getName());
        }
        if (itemDto.getDescription() != null && !itemDto.getDescription().isBlank()) {
            item.setDescription(itemDto.getDescription());
        }
        if (itemDto.getAvailable() != null) {
            item.setAvailable(itemDto.getAvailable());
        }

        return itemMapper.toDto(itemRepository.save(item));
    }



    @Override
    @Transactional
    public CommentDto addComment(Long userId, Long itemId, CreateCommentRequest request) {
        User author = findUserById(userId);

        Item item = findItemById(itemId);

        boolean hasBooked = bookingRepository.existsByBookerIdAndItemIdAndEndBeforeAndStatus(
                userId, itemId, LocalDateTime.now(), BookingStatus.APPROVED
        );

        if (!hasBooked) {
            throw new ValidationException("Пользователь не может прокомментировать данную вещь");
        }

        Comment comment = new Comment(
                request.getText(),
                item,
                author,
                LocalDateTime.now()
        );

        Comment savedComment = commentRepository.save(comment);

        item.getComments().add(comment);
        itemRepository.save(item);

        return commentMapper.toDto(savedComment);
    }


    private ItemForOwnerDto mapToItemDtoForOwner(Item item) {
        ItemForOwnerDto itemDtoForOwner = itemMapper.toDtoForOwner(item);

        List<Booking> bookings = bookingRepository.findByItemIdAndStatusInOrderByStartAsc(
                item.getId(), List.of(BookingStatus.APPROVED)
        );

        LocalDateTime now = LocalDateTime.now();

        Booking lastBooking = bookings.stream()
                .filter(b -> b.getEnd().isBefore(now))
                .reduce((first, second) -> second)
                .orElse(null);

        Booking nextBooking = bookings.stream()
                .filter(b -> b.getStart().isAfter(now))
                .findFirst()
                .orElse(null);

        if (lastBooking != null) {
            itemDtoForOwner.setLastBooking(new BookingShortDto(
                    lastBooking.getId(),
                    lastBooking.getBooker().getId(),
                    lastBooking.getStart(),
                    lastBooking.getEnd()
            ));
        }

        if (nextBooking != null) {
            itemDtoForOwner.setNextBooking(new BookingShortDto(
                    nextBooking.getId(),
                    nextBooking.getBooker().getId(),
                    nextBooking.getStart(),
                    nextBooking.getEnd()
            ));
        }

        return itemDtoForOwner;
    }

    private void checkUserExists(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("Пользователь не найден");
        }
    }

    private void checkOwner(Item item, Long userId) {
        if (!item.getOwner().getId().equals(userId)) {
            throw new NotOwnerException("Пользователь не является владельцем предмета");
        }
    }

}
