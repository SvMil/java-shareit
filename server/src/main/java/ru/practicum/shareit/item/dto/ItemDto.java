package ru.practicum.shareit.item.dto;


import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import ru.practicum.shareit.item.comment.CommentDto;
import ru.practicum.shareit.booking.dto.BookingShortDto;


import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemDto {
    private Long id;
    private String name;
    private String description;
    private Boolean available;
    private Long requestId;
    private List<CommentDto> comments = new ArrayList<>();
    private BookingShortDto lastBooking;
    private BookingShortDto nextBooking;

    public ItemDto(Long id, String name, String description, Boolean available, Long requestId, List<CommentDto> comments) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.available = available;
        this.requestId = requestId;
        this.comments = comments;
    }
}
