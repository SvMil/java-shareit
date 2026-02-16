package ru.practicum.shareit.request.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.request.ItemRequestDto;
import ru.practicum.shareit.request.ItemRequestMapper;
import ru.practicum.shareit.request.ItemRequestRepository;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.User;

import java.util.List;
import java.time.LocalDateTime;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ItemRequestServiceImpl implements ItemRequestService {
    private final ItemRequestRepository itemRequestRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    @Override
    public List<ItemRequestDto> getAllRequests(Long userId) {
        findUserById(userId);

        return ItemRequestMapper.toDto(itemRequestRepository.findByRequestorIdNotOrderByCreatedDesc(userId));
    }

    @Override
    public List<ItemRequestDto> getRequests(Long userId) {
        findUserById(userId);

        return ItemRequestMapper.toDto(itemRequestRepository.findByRequestorIdOrderByCreatedDesc(userId));
    }

    @Override
    public ItemRequestDto getRequestById(Long userId, Long requestId) {
        findUserById(userId);

        ItemRequestDto dto = ItemRequestMapper.toDto(findItemRequestById(requestId));

        List<Item> items = itemRepository.findByRequestId(requestId);

        dto.setItems(ItemRequestMapper.toShortDto(items));

        return dto;
    }

    @Override
    @Transactional
    public ItemRequestDto createRequest(Long userId, ItemRequestDto requestDto) {
        User user = findUserById(userId);

        ItemRequest itemRequest = ItemRequestMapper.toEntity(requestDto);
        itemRequest.setRequestor(user);
        itemRequest.setCreated(LocalDateTime.now());

        return ItemRequestMapper.toDto(itemRequestRepository.save(itemRequest));
    }

    private User findUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден"));
    }

    public ItemRequest findItemRequestById(Long requestId) {
        return itemRequestRepository.findById(requestId)
                .orElseThrow(() -> new NotFoundException("Запрос вещи не найден"));
    }
}
