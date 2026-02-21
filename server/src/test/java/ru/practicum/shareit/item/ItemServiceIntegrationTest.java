package ru.practicum.shareit.item;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.item.comment.CommentDto;
import ru.practicum.shareit.item.comment.CreateCommentRequest;
import ru.practicum.shareit.item.dto.ItemForOwnerDto;
import ru.practicum.shareit.item.dto.ItemUpdateDto;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.request.service.ItemRequestService;
import ru.practicum.shareit.booking.service.StateStatus;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.item.dto.ItemDto;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class ItemServiceIntegrationTest {

    @Autowired
    private ItemService itemService;

    @Autowired
    private UserService userService;

    @Autowired
    private BookingService bookingService;

    @Autowired
    private ItemRequestService itemRequestService;

    private UserDto owner;
    private UserDto booker;

    @BeforeEach
    void setUp() {
        owner = userService.addUser(new UserDto(null, "Owner", "owner@test.com"));
        booker = userService.addUser(new UserDto(null, "Booker", "booker@test.com"));
    }

    @Test
    void getUserItemsPositive() {
        ItemDto itemDto = new ItemDto(1L, "Коньки", "Немного большемерят", true, null, null);
        ItemDto createdItem = itemService.addItem(itemDto, owner.getId());

        BookingRequestDto bookingDto = new BookingRequestDto(
            createdItem.getId(),
            LocalDateTime.now().plusDays(1),
            LocalDateTime.now().plusDays(2)
        );
        bookingService.createBooking(bookingDto, booker.getId());
        Long bookingId = bookingService.getUserBookings(booker.getId(), StateStatus.ALL).get(0).getId();
        bookingService.updateBookingStatus(bookingId, owner.getId(), true);
        List<ItemForOwnerDto> result = itemService.getItemsByOwner(owner.getId());

        assertEquals(1, result.size());
        ItemForOwnerDto item = result.get(0);
        assertEquals("Коньки", item.getName());
        assertNotNull(item.getNextBooking());
    }

    @Test
    void getUserItemsNegative() {
        List<ItemForOwnerDto> result = itemService.getItemsByOwner(owner.getId());

        assertTrue(result.isEmpty());
    }

    @Test
    void searchItemsPositive() {
        itemService.addItem(new ItemDto(null, "Коньки", "Удобные", true, null, null), owner.getId());
        itemService.addItem(new ItemDto(null, "Ролики", "С подсветкой", true, null, null), owner.getId());
        List<ItemDto> result = itemService.searchItems("коньки");

        assertEquals(1, result.size());
        assertEquals("Коньки", result.get(0).getName());
    }

    @Test
    void searchItemsNegative() {
        itemService.addItem(new ItemDto(null, "Дрель", "Мощная дрель", true, null, null), owner.getId());
        List<ItemDto> result = itemService.searchItems("");

        assertTrue(result.isEmpty());
    }

    @Test
    void addComment() throws InterruptedException {
        ItemDto itemDto = new ItemDto(null, "Ролики", "С подсветкой", true, null, null);
        ItemDto createdItem = itemService.addItem(itemDto, owner.getId());

        BookingRequestDto bookingDto = new BookingRequestDto(
                createdItem.getId(),
                LocalDateTime.now().minusDays(2),
                LocalDateTime.now().minusDays(1)
        );
        bookingService.createBooking(bookingDto, booker.getId());

        Long bookingId = bookingService.getUserBookings(booker.getId(), StateStatus.ALL).get(0).getId();
        bookingService.updateBookingStatus(bookingId, owner.getId(), true);
        Thread.sleep(100);
        CreateCommentRequest commentDto = new CreateCommentRequest();
        commentDto.setText("Огнище!");
        CommentDto result = itemService.addComment(booker.getId(), createdItem.getId(), commentDto);

        assertNotNull(result);
        assertNotNull(result.getId());
        assertEquals("Огнище!", result.getText());
        assertEquals("Booker", result.getAuthorName());
        assertNotNull(result.getCreated());
    }


    @Test
    void getItem() {
        ItemDto itemDto = new ItemDto(1L, "Коньки", "Почти новые", true, null, null);
        ItemDto createdItem = itemService.addItem(itemDto, owner.getId());
        ItemDto result = itemService.getItemById(createdItem.getId());

        assertNotNull(result);
        assertEquals("Коньки", result.getName());
    }

    @Test
    void createItem() {
        ItemDto itemDto = new ItemDto(1L, "Коньки", "Хорошие, удобные, наточены", true, null, null);
        ItemDto result = itemService.addItem(itemDto, owner.getId());

        assertNotNull(result);
        assertNotNull(result.getId());
        assertEquals("Коньки", result.getName());
        assertEquals("Хорошие, удобные, наточены", result.getDescription());
        assertTrue(result.getAvailable());
    }

    @Test
    void createItemWithRequest() {
        ru.practicum.shareit.request.ItemRequestDto requestDto =
                new ru.practicum.shareit.request.ItemRequestDto();
        requestDto.setDescription("Нужны коньки 37 размер");
        ru.practicum.shareit.request.ItemRequestDto request =
                itemRequestService.createRequest(owner.getId(), requestDto);

        ItemDto itemDto = new ItemDto(null, "Коньки", "Удобные, немного большемерят", true, request.getId(), null);
        ItemDto result = itemService.addItem(itemDto, owner.getId());

        assertNotNull(result);
        assertEquals(request.getId(), result.getRequestId());
    }

    @Test
    void updateItem() {
        ItemDto itemDto = new ItemDto(null, "Коньки", "Описание коньков", true, null, null);
        ItemDto createdItem = itemService.addItem(itemDto, owner.getId());
        ItemUpdateDto updateDto = new ItemUpdateDto(null, "Обновленное описание коньков", null, null, null);
        ItemDto result = itemService.updateItem(createdItem.getId(), updateDto, owner.getId());

        assertEquals("Обновленное описание коньков", result.getName());
        assertEquals("Описание коньков", result.getDescription());
        assertTrue(result.getAvailable());
    }
}

