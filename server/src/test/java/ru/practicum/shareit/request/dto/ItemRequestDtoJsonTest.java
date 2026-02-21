package ru.practicum.shareit.request.dto;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.json.JsonContent;
import org.springframework.boot.test.json.JacksonTester;
import ru.practicum.shareit.request.ItemRequestDto;

import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class ItemRequestDtoJsonTest {

    @Autowired
    private JacksonTester<ItemRequestDto> json;

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    @Test
    void testSerialize() throws Exception {
        LocalDateTime created = LocalDateTime.of(2026, 2, 20, 23, 0);
        ru.practicum.shareit.item.dto.ItemShortResponseDto itemInfo = new ru.practicum.shareit.item.dto.ItemShortResponseDto(1L, "Коньки", 2L);
        ItemRequestDto dto = new ItemRequestDto(1L, "Срочно нужны коньки", created, List.of(itemInfo));
        JsonContent<ItemRequestDto> result = json.write(dto);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.description").isEqualTo("Срочно нужны коньки");
        assertThat(result).extractingJsonPathStringValue("$.created")
            .isEqualTo(created.format(FORMATTER));
        assertThat(result).extractingJsonPathArrayValue("$.items").hasSize(1);
        assertThat(result).extractingJsonPathNumberValue("$.items[0].id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.items[0].name").isEqualTo("Коньки");
        assertThat(result).extractingJsonPathNumberValue("$.items[0].ownerId").isEqualTo(2);
    }

    @Test
    void testDeserialize() throws Exception {
        String content = "{\"id\":1,\"description\":\"Срочно нужны коньки\",\"created\":\"2026-02-20T23:00:00\"," +
                        "\"items\":[{\"id\":1,\"name\":\"Коньки\",\"ownerId\":2}]}";
        ItemRequestDto result = json.parse(content).getObject();

        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getDescription()).isEqualTo("Срочно нужны коньки");
        assertThat(result.getCreated()).isEqualTo(LocalDateTime.of(2026, 2, 20, 23, 0));
        assertThat(result.getItems()).hasSize(1);
        assertThat(result.getItems().get(0).getId()).isEqualTo(1L);
        assertThat(result.getItems().get(0).getName()).isEqualTo("Коньки");
        assertThat(result.getItems().get(0).getOwnerId()).isEqualTo(2L);
    }

    @Test
    void testSerializeWithEmptyItems() throws Exception {
        LocalDateTime created = LocalDateTime.of(2026, 2, 20, 23, 0);
        ItemRequestDto dto = new ItemRequestDto(1L, "Срочно нужны коньки", created, List.of());
        JsonContent<ItemRequestDto> result = json.write(dto);

        assertThat(result).extractingJsonPathArrayValue("$.items").isEmpty();
    }
}

