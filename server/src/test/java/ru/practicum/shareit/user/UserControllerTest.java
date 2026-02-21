package ru.practicum.shareit.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.exception.EmailAlreadyExistsException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.dto.UserUpdateDto;
import ru.practicum.shareit.user.service.UserService;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    @Test
    void getUserPositive() throws Exception {
        UserDto userDto = new UserDto(1L, "Prosto Kot", "kot@test.com");
        when(userService.getUserById(anyLong())).thenReturn(userDto);

        mockMvc.perform(get("/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Prosto Kot"))
                .andExpect(jsonPath("$.email").value("kot@test.com"));
    }

    @Test
    void getUserNegative() throws Exception {
        when(userService.getUserById(anyLong()))
                .thenThrow(new NotFoundException("Пользователь не найден"));

        mockMvc.perform(get("/users/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void getAllUsersPositive() throws Exception {
        List<UserDto> users = List.of(
                new UserDto(1L, "User 1", "user1@test.com"),
                new UserDto(2L, "User 2", "user2@test.com")
        );
        when(userService.getUsers()).thenReturn(users);

        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].name").value("User 1"))
                .andExpect(jsonPath("$[1].name").value("User 2"));
    }

    @Test
    void createUserPositive() throws Exception {
        UserDto userDto = new UserDto(null, "Prosto Kot", "kot@test.com");
        UserDto responseDto = new UserDto(1L, "Prosto Kot", "kot@test.com");
        when(userService.addUser(any(UserDto.class))).thenReturn(responseDto);

        mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Prosto Kot"))
                .andExpect(jsonPath("$.email").value("kot@test.com"));
    }

    @Test
    void createUserNegative() throws Exception {
        UserDto userDto = new UserDto(null, "Prosto Kot", "kot@test.com");
        when(userService.addUser(any(UserDto.class)))
            .thenThrow(new EmailAlreadyExistsException("Email уже используется"));

        mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isConflict());
    }

    @Test
    void updateUserPositive() throws Exception {
        UserDto updateDto = new UserDto(null, "Kot Updated", null);
        UserDto responseDto = new UserDto(1L, "Kot Updated", "kot@test.com");
        when(userService.updateUser(anyLong(), any(UserUpdateDto.class))).thenReturn(responseDto);

        mockMvc.perform(patch("/users/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Kot Updated"));
    }

    @Test
    void deleteUser() throws Exception {
        mockMvc.perform(delete("/users/1"))
                .andExpect(status().isOk());
    }
}

