package ru.practicum.shareit.user.service;

import ru.practicum.shareit.BaseService;
import ru.practicum.shareit.user.dto.request.create.UserRequestCreateDto;
import ru.practicum.shareit.user.dto.request.update.UserRequestUpdateDto;
import ru.practicum.shareit.user.dto.response.UserResponseDto;

public interface UserService extends BaseService<UserRequestCreateDto, UserRequestUpdateDto, UserResponseDto> {
}
