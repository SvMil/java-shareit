package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import ru.practicum.shareit.item.comment.CommentDto;
import ru.practicum.shareit.booking.dto.BookingShortDto;


import java.util.List;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class ItemFullResponseDto {
    private Long id;
    private String name;
    private String description;
    private Boolean available;
    private BookingShortDto lastBooking;
    private BookingShortDto nextBooking;
    private List<CommentDto> comments;

    public ItemFullResponseDto(Long id, String name, String description, Boolean available) {
    }
}
