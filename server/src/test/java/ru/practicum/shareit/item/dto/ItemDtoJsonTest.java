package ru.practicum.shareit.item.dto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class ItemDtoJsonTest {

    @Autowired
    private JacksonTester<ItemDto> json;

    @Test
    void testSerialize() throws Exception {
        // Given
        ItemDto dto = new ItemDto(1L, "Коньки", "Удобные, немного большемерят", true, 2L, null);

        // When
        JsonContent<ItemDto> result = json.write(dto);

        // Then
        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.name").isEqualTo("Коньки");
        assertThat(result).extractingJsonPathStringValue("$.description").isEqualTo("Удобные, немного большемерят");
        assertThat(result).extractingJsonPathBooleanValue("$.available").isTrue();
        assertThat(result).extractingJsonPathNumberValue("$.requestId").isEqualTo(2);
    }

    @Test
    void testDeserialize() throws Exception {
        // Given
        String content = "{\"id\":1,\"name\":\"Коньки\",\"description\":\"Удобные, немного большемерят\"," +
                        "\"available\":true,\"requestId\":2}";

        // When
        ItemDto result = json.parse(content).getObject();

        // Then
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getName()).isEqualTo("Коньки");
        assertThat(result.getDescription()).isEqualTo("Удобные, немного большемерят");
        assertThat(result.getAvailable()).isTrue();
        assertThat(result.getRequestId()).isEqualTo(2L);
    }

    @Test
    void testSerializeWithoutRequest() throws Exception {
        // Given
        ItemDto dto = new ItemDto(1L, "Коньки", "Удобные, немного большемерят", true, null, null);

        // When
        JsonContent<ItemDto> result = json.write(dto);

        // Then
        assertThat(result).extractingJsonPathNumberValue("$.requestId").isNull();
    }

    @Test
    void testDeserializeWithoutRequest() throws Exception {
        // Given
        String content = "{\"id\":1,\"name\":\"Коньки\",\"description\":\"Удобные, немного большемерят\"," +
                        "\"available\":true,\"requestId\":null}";

        // When
        ItemDto result = json.parse(content).getObject();

        // Then
        assertThat(result.getRequestId()).isNull();
    }
}

