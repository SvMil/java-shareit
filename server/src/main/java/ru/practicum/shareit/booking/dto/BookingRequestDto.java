package ru.practicum.shareit.booking.dto;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;


import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookingRequestDto {

    @NotNull
    private Long itemId;

    @NotNull
    @FutureOrPresent(message = "Начало бронирования должно быть в будущем")
    private LocalDateTime start;

    @NotNull
    @FutureOrPresent(message = "Конец бронирования должен быть в будущем")
    private LocalDateTime end;
}
