package ru.practicum.shareit.request.model;

import ru.practicum.shareit.user.model.User;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ItemRequest {
    private Long id;
    private String description;
    private User requestor;
    private LocalDateTime created;
}
