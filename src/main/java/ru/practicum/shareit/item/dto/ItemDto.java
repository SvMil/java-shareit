package ru.practicum.shareit.item.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.AllArgsConstructor;

@Data
@AllArgsConstructor
public class ItemDto {
    private Long id;

    @NotBlank
    private String name;

    @NotBlank
    private String description;

    @NotNull
    private Boolean available;
    private Long request;
}
