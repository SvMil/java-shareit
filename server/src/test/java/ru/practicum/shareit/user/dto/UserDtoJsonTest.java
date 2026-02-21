package ru.practicum.shareit.user.dto;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class UserDtoJsonTest {

    @Autowired
    private JacksonTester<UserDto> json;

    @Test
    void testSerialize() throws Exception {
        UserDto dto = new UserDto(1L, "Prosto Kot", "kot@test.com");
        JsonContent<UserDto> result = json.write(dto);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.name").isEqualTo("Prosto Kot");
        assertThat(result).extractingJsonPathStringValue("$.email").isEqualTo("kot@test.com");
    }

    @Test
    void testDeserialize() throws Exception {
        String content = "{\"id\":null,\"name\":\"Prosto Kot\",\"email\":\"kot@test.com\"}";
        UserDto result = json.parse(content).getObject();

        assertThat(result.getId()).isNull();
        assertThat(result.getName()).isEqualTo("Prosto Kot");
        assertThat(result.getEmail()).isEqualTo("kot@test.com");
    }

    @Test
    void testDeserializeWithId() throws Exception {
        String content = "{\"id\":1,\"name\":\"Prosto Kot\",\"email\":\"kot@test.com\"}";
        UserDto result = json.parse(content).getObject();

        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getName()).isEqualTo("Prosto Kot");
        assertThat(result.getEmail()).isEqualTo("kot@test.com");
    }
}

