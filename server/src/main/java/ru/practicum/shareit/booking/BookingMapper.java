package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.dto.BookingShortDto;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.model.Booking;

public class BookingMapper {

    public static BookingResponseDto toDto(Booking booking) {
        BookingResponseDto bookingResponseDto = new BookingResponseDto();
        bookingResponseDto.setId(booking.getId());
        bookingResponseDto.setStart(booking.getStart());
        bookingResponseDto.setEnd(booking.getEnd());
        bookingResponseDto.setStatus(booking.getStatus());

        bookingResponseDto.setBooker(UserMapper.toDto(booking.getBooker()));
        bookingResponseDto.setItem(ItemMapper.toDto(booking.getItem()));

        return bookingResponseDto;
    }

    public static BookingShortDto toBookingShortDto(Booking booking) {
        return new BookingShortDto(booking.getId(), booking.getBooker().getId(), booking.getStart(), booking.getEnd());
    }

    public static Booking toEntity(BookingRequestDto bookingRequestDto) {
        Booking booking = new Booking();
        booking.setStart(bookingRequestDto.getStart());
        booking.setEnd(bookingRequestDto.getEnd());

        return booking;
    }
}
