package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;

import java.util.List;

public interface BookingService {
    BookingResponseDto getBookingById(Long bookingId, Long userId);

    BookingResponseDto createBooking(BookingRequestDto bookingRequestDto, Long userId);

    BookingResponseDto updateBookingStatus(Long bookingId, Long ownerId, boolean approved);

    List<BookingResponseDto> getUserBookings(Long userId, StateStatus state);

    List<BookingResponseDto> getOwnerBookings(Long ownerId, StateStatus state);
}
