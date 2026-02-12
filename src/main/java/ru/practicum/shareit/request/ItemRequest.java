package ru.practicum.shareit.request;

import lombok.Data;
import ru.practicum.shareit.user.User;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Data
@NoArgsConstructor
public class ItemRequest {
    private Long id;
    private String description;
    private User requestor;
    private LocalDateTime created;
}
