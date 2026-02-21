package ru.practicum.shareit.user;

import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserUpdateDto;
import ru.practicum.shareit.user.service.UserService;
import ru.practicum.shareit.exception.NotFoundException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class UserServiceIntegrationTest {

    @Autowired
    private UserService userService;

    @Test
    void createUserPositive() {
        UserDto userDto = new UserDto(null, "Prosto Kot", "kot@test.com");
        UserDto result = userService.addUser(userDto);

        assertNotNull(result);
        assertNotNull(result.getId());
        assertEquals("Prosto Kot", result.getName());
        assertEquals("kot@test.com", result.getEmail());
    }

    @Test
    void createUserNegative() {
        UserDto userDto1 = new UserDto(null, "Prosto Kot", "kot@test.com");
        UserDto userDto2 = new UserDto(null, "Neprosto Kot", "kot@test.com");
        userService.addUser(userDto1);

        assertThrows(ru.practicum.shareit.exception.EmailAlreadyExistsException.class, () ->
            userService.addUser(userDto2)
        );
    }

    @Test
    void updateUserNamePositive() {
        UserDto userDto = new UserDto(null, "Prosto Kot", "kot@test.com");
        UserDto created = userService.addUser(userDto);
        UserUpdateDto updateDto = new UserUpdateDto("John Updated", "kot@test.com");
        UserDto result = userService.updateUser(created.getId(), updateDto);

        assertEquals("John Updated", result.getName());
        assertEquals("kot@test.com", result.getEmail()); // Не изменился
    }

    @Test
    void updateUserEmailPositive() {
        UserDto userDto = new UserDto(null, "Prosto Kot", "kot@test.com");
        UserDto created = userService.addUser(userDto);
        UserUpdateDto updateDto = new UserUpdateDto("Prosto Kot", "updatekotemail@test.com");
        UserDto result = userService.updateUser(created.getId(), updateDto);

        assertEquals("Prosto Kot", result.getName());
        assertEquals("updatekotemail@test.com", result.getEmail());
    }

    @Test
    void updateUserNdegative() {
        UserDto user1 = userService.addUser(new UserDto(null, "Kot 1", "kot1@test.com"));
        UserDto user2 = userService.addUser(new UserDto(null, "Kot 2", "kot2@test.com"));
        UserUpdateDto updateDto = new UserUpdateDto("Kot 2", "kot1@test.com");

        assertThrows(ru.practicum.shareit.exception.EmailAlreadyExistsException.class, () ->
            userService.updateUser(user2.getId(), updateDto)
        );
    }

    @Test
    void getUserPositive() {
        UserDto userDto = new UserDto(null, "Prosto Kot", "kot@test.com");
        UserDto created = userService.addUser(userDto);
        UserDto result = userService.getUserById(created.getId());

        assertEquals(created.getId(), result.getId());
        assertEquals("Prosto Kot", result.getName());
        assertEquals("kot@test.com", result.getEmail());
    }

    @Test
    void getUserNegative() {
        Long nonExistentId = 123L;

        assertThrows(NotFoundException.class, () ->
            userService.getUserById(nonExistentId)
        );
    }

    @Test
    void getAllUsers() {
        userService.addUser(new UserDto(null, "Kot 1", "kot1@test.com"));
        userService.addUser(new UserDto(null, "Kot 2", "kot2@test.com"));
        List<UserDto> result = userService.getUsers();

        assertEquals(2, result.size());
    }

    @Test
    void deleteUser() {
        UserDto userDto = new UserDto(null, "Prosto Kot", "kot@test.com");
        UserDto created = userService.addUser(userDto);
        userService.deleteUser(created.getId());

        assertThrows(NotFoundException.class, () ->
            userService.getUserById(created.getId())
        );
    }
}

