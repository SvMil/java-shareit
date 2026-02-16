package ru.practicum.shareit.booking;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import org.springframework.beans.factory.annotation.Autowired;


import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;


@WebMvcTest(BookingController.class)
class BookingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private BookingService bookingService;

    private final BookingResponseDto bookingForTests = new BookingResponseDto(1L,
            LocalDateTime.now(),
            LocalDateTime.now().plusHours(24),
            null,
            null,
            null);

    @SneakyThrows
    @Test
    void getBookingById() {
        when(bookingService.getBookingById(anyLong(), anyLong())).thenReturn(bookingForTests);

        String result = mockMvc.perform(get("/bookings/1")
                        .header("X-Sharer-User-Id", "123"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(objectMapper.writeValueAsString(bookingForTests), result);
    }

    @SneakyThrows
    @Test
    void createBooking_whenSuccess() {
        BookingRequestDto bookingSaveDto = new BookingRequestDto(1L, LocalDateTime.now().plusDays(2), LocalDateTime.now().plusDays(6));
        when(bookingService.createBooking(any(BookingRequestDto.class), anyLong())).thenReturn(bookingForTests);

        String result = mockMvc.perform(post("/bookings")
                        .header("X-Sharer-User-Id", "123")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(bookingSaveDto)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(objectMapper.writeValueAsString(bookingForTests), result);
    }

    @SneakyThrows
    @Test
    void updateBookingStatus() {
        when(bookingService.updateBookingStatus(anyLong(), anyLong(), anyBoolean())).thenReturn(bookingForTests);

        String result = mockMvc.perform(patch("/bookings/1")
                        .param("approved", "true")
                        .header("X-Sharer-User-Id", "123"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(objectMapper.writeValueAsString(bookingForTests), result);
    }


}