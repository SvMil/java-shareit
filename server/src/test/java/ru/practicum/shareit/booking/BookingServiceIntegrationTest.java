package ru.practicum.shareit.booking;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.booking.service.StateStatus;
import ru.practicum.shareit.exception.NotOwnerException;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.item.dto.ItemDto;


import java.util.List;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class BookingServiceIntegrationTest {

    @Autowired
    private ItemService itemService;

    @Autowired
    private UserService userService;

    @Autowired
    private BookingService bookingService;


    private UserDto booker;
    private UserDto owner;
    private ItemDto item;

    @BeforeEach
    void setUp() {
        booker = userService.addUser(new UserDto(null, "Booker", "booker@test.com"));
        owner = userService.addUser(new UserDto(null, "Owner", "owner@test.com"));
        item = itemService.addItem(
            new ItemDto(1L, "Дрель", "Мощная дрель", true, null, null),
            owner.getId()
        );
    }

    @Test
    void getOwnerBooking() {
        BookingRequestDto bookingDto = new BookingRequestDto(
                item.getId(),
                LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(2)
        );
        BookingResponseDto created = bookingService.createBooking(bookingDto, booker.getId());
        BookingResponseDto result = bookingService.getBookingById(created.getId(), owner.getId());

        assertNotNull(result);
        assertEquals(created.getId(), result.getId());
    }

    @Test
    void getBookersBooking() {
        BookingRequestDto bookingDto = new BookingRequestDto(
                item.getId(),
                LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(2)
        );
        BookingResponseDto created = bookingService.createBooking(bookingDto, booker.getId());
        BookingResponseDto result = bookingService.getBookingById(created.getId(), booker.getId());

        assertNotNull(result);
        assertEquals(created.getId(), result.getId());
    }

    @Test
    void getOwnerAllBookings() {
        bookingService.createBooking(
                new BookingRequestDto(item.getId(), LocalDateTime.now().plusDays(1), LocalDateTime.now().plusDays(2)),
                booker.getId()
        );
        List<BookingResponseDto> result = bookingService.getOwnerBookings(owner.getId(), StateStatus.ALL);

        assertEquals(1, result.size());
    }

    @Test
    void getOwnerFutureBookings() {
        bookingService.createBooking(
                new BookingRequestDto(item.getId(), LocalDateTime.now().plusDays(1), LocalDateTime.now().plusDays(2)),
                booker.getId()
        );
        List<BookingResponseDto> result = bookingService.getOwnerBookings(owner.getId(), StateStatus.FUTURE);

        assertEquals(1, result.size());
    }

    @Test
    void getUserAllBookings() {
        bookingService.createBooking(
                new BookingRequestDto(item.getId(), LocalDateTime.now().plusDays(1), LocalDateTime.now().plusDays(2)),
                booker.getId()
        );
        List<BookingResponseDto> result = bookingService.getUserBookings(booker.getId(), StateStatus.ALL);

        assertEquals(1, result.size());
    }

    @Test
    void getUserWaitingBookings() {
        bookingService.createBooking(
                new BookingRequestDto(item.getId(), LocalDateTime.now().plusDays(1), LocalDateTime.now().plusDays(2)),
                booker.getId()
        );
        List<BookingResponseDto> result = bookingService.getUserBookings(booker.getId(), StateStatus.WAITING);

        assertEquals(1, result.size());
        assertEquals(BookingStatus.WAITING, result.get(0).getStatus());
    }

    @Test
    void addBookingPositive() {
        BookingRequestDto bookingDto = new BookingRequestDto(
            item.getId(),
            LocalDateTime.now().plusDays(1),
            LocalDateTime.now().plusDays(2)
        );
        BookingResponseDto result = bookingService.createBooking(bookingDto, booker.getId());

        assertNotNull(result);
        assertNotNull(result.getId());
        assertEquals(item.getId(), result.getItem().getId());
        assertEquals(booker.getId(), result.getBooker().getId());
        assertEquals(BookingStatus.WAITING, result.getStatus());
    }

    @Test
    void addBookingNegative() {
        BookingRequestDto bookingDto = new BookingRequestDto(
            item.getId(),
            LocalDateTime.now().plusDays(1),
            LocalDateTime.now().plusDays(2)
        );

        assertThrows(NotOwnerException.class, () ->
            bookingService.createBooking(bookingDto, owner.getId())
        );
    }

    @Test
    void approveBooking() {
        BookingRequestDto bookingDto = new BookingRequestDto(
            item.getId(),
            LocalDateTime.now().plusDays(1),
            LocalDateTime.now().plusDays(2)
        );
        BookingResponseDto created = bookingService.createBooking(bookingDto, booker.getId());
        BookingResponseDto result = bookingService.updateBookingStatus(created.getId(), owner.getId(), true);

        assertEquals(BookingStatus.APPROVED, result.getStatus());
    }

    @Test
    void rejectBooking() {
        BookingRequestDto bookingDto = new BookingRequestDto(
            item.getId(),
            LocalDateTime.now().plusDays(1),
            LocalDateTime.now().plusDays(2)
        );
        BookingResponseDto created = bookingService.createBooking(bookingDto, booker.getId());
        BookingResponseDto result = bookingService.updateBookingStatus(created.getId(), owner.getId(), false);

        assertEquals(BookingStatus.REJECTED, result.getStatus());
    }
}

