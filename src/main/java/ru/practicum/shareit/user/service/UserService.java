package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserUpdateDto;

import java.util.List;

public interface UserService {

    List<UserDto> getUsers();

    UserDto getUserById(Long id);

    UserDto addUser(UserDto userDto);

    void deleteUser(Long id);

    UserDto updateUser(Long userId, UserUpdateDto newUserDto);
}
