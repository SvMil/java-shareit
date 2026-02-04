package ru.practicum.shareit.user.dao;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.dto.UserUpdateDto;
import ru.practicum.shareit.user.model.User;

import java.util.HashMap;
import java.util.List;

@Repository
public class UserRepository {
    private long counterId = 1;
    private static final HashMap<Long, User> users = new HashMap<>();


    public List<UserDto> getUsers() {
        return UserMapper.toDto(users.values());
    }

    public UserDto getUserById(Long id) {
        return UserMapper.toDto(users.get(id));
    }

    public UserDto addUser(UserDto userDto) {
        userDto.setId(counterId++);
        users.put(userDto.getId(), UserMapper.toEntity(userDto));
        return userDto;
    }

    public void deleteUser(Long id) {
        users.remove(id);
    }

    public UserDto updateUser(Long userId, UserUpdateDto newUserDto) {
        User user = users.get(userId);
        if (newUserDto.getName() != null) {
            user.setName(newUserDto.getName());
        }
        if (newUserDto.getEmail() != null) {
            user.setEmail(newUserDto.getEmail());
        }
        return UserMapper.toDto(user);
    }

    public boolean existsById(Long id) {
        return users.containsKey(id);
    }

    public boolean existsByEmail(String email) {
        return users.values().stream()
                .anyMatch(user -> user.getEmail().equals(email));
    }
}
