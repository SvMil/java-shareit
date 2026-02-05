package ru.practicum.shareit.user;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;
import java.util.Collection;

@Component
public class UserMapper {

    public User toEntity(UserDto userDto) {
        return new User(userDto.getId(), userDto.getName(), userDto.getEmail());
    }

    public UserDto toDto(User user) {
        return new UserDto(user.getId(), user.getName(), user.getEmail());
    }

    public List<UserDto> toDto(Collection<User> users) {
        return users.stream()
                .map(this::toDto)
                .toList();
    }

    }
