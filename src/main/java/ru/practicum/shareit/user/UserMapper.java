package ru.practicum.shareit.user;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.ArrayList;
import java.util.List;
import java.util.Collection;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserMapper {

    public static User toEntity(UserDto userDto) {
        return new User(userDto.getId(), userDto.getName(), userDto.getEmail());
    }

    public static UserDto toDto(User user) {
        return new UserDto(user.getId(), user.getName(), user.getEmail());
    }

    public static List<UserDto> toDto(Collection<User> users) {
        List<UserDto> result = new ArrayList<>();

        for (User user : users) {
            result.add(toDto(user));
        }

        return result;
    }
    }
