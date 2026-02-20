package ru.practicum.shareit.item.dto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.item.comment.CommentDto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class CommentDtoJsonTest {

    @Autowired
    private JacksonTester<CommentDto> json;

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    @Test
    void testSerialize() throws Exception {
        // Given
        LocalDateTime created = LocalDateTime.of(2026, 2, 20, 23, 0);
        CommentDto dto = new CommentDto(1L, "Коньки огнище!", "Человек с ценным мнением", created);

        // When
        JsonContent<CommentDto> result = json.write(dto);

        // Then
        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.text").isEqualTo("Коньки огнище!");
        assertThat(result).extractingJsonPathStringValue("$.authorName").isEqualTo("Человек с ценным мнением");
        assertThat(result).extractingJsonPathStringValue("$.created")
            .isEqualTo(created.format(FORMATTER));
    }

    @Test
    void testDeserialize() throws Exception {
        // Given
        String content = "{\"id\":1,\"text\":\"Коньки огнище!\",\"authorName\":\"Человек с ценным мнением\"," +
                        "\"created\":\"2026-02-20T23:00:00\"}";

        // When
        CommentDto result = json.parse(content).getObject();

        // Then
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getText()).isEqualTo("Коньки огнище!");
        assertThat(result.getAuthorName()).isEqualTo("Человек с ценным мнением");
        assertThat(result.getCreated()).isEqualTo(LocalDateTime.of(2026, 2, 20, 23, 0));
    }
}

