package ru.practicum.shareit.item.dto;


import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import ru.practicum.shareit.booking.dto.BookingShortDto;
import ru.practicum.shareit.item.comment.CommentDto;

import java.util.List;
import java.util.ArrayList;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class ItemDto {
    private Long id;

    private String name;

    private String description;

    private Boolean available;
    private Long request;
    private List<CommentDto> comments = new ArrayList<>();
    private BookingShortDto lastBooking;
    private BookingShortDto nextBooking;

    public ItemDto(Long id, String name, String description, Boolean available, Long request, List<CommentDto> comments) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.available = available;
        this.request = request;
        this.comments = comments;
    }
}
