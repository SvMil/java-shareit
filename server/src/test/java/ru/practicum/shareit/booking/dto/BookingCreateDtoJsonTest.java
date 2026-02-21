package ru.practicum.shareit.booking.dto;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.json.JsonContent;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.beans.factory.annotation.Autowired;


import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class BookingCreateDtoJsonTest {

    @Autowired
    private JacksonTester<BookingRequestDto> json;

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    @Test
    void testSerialize() throws Exception {
        LocalDateTime start = LocalDateTime.of(2026, 2, 19, 13, 30);
        LocalDateTime end = LocalDateTime.of(2026, 2, 20, 14, 30);

        BookingRequestDto dto = new BookingRequestDto(1L, start, end);
        JsonContent<BookingRequestDto> result = json.write(dto);

        assertThat(result).extractingJsonPathNumberValue("$.itemId").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.start")
            .isEqualTo(start.format(FORMATTER));
        assertThat(result).extractingJsonPathStringValue("$.end")
            .isEqualTo(end.format(FORMATTER));
    }

    @Test
    void testDeserialize() throws Exception {
        String content = "{\"itemId\":1,\"start\":\"2026-02-19T13:30:00\",\"end\":\"2026-02-20T14:30:00\"}";
        BookingRequestDto result = json.parse(content).getObject();

        assertThat(result.getItemId()).isEqualTo(1L);
        assertThat(result.getStart()).isEqualTo(LocalDateTime.of(2026, 2, 19, 13, 30));
        assertThat(result.getEnd()).isEqualTo(LocalDateTime.of(2026, 2, 20, 14, 30));
    }
}

