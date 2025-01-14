package ru.practicum.shareit.user;

import ru.practicum.shareit.user.dto.UserCreateDto;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserUpdateDto;

public interface UserService {
    UserDto create(UserCreateDto dto);

    UserDto findById(long id);

    UserDto update(UserUpdateDto dto);

    void delete(long id);
}
