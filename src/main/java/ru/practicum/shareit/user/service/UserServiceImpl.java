package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.dto.request.create.UserRequestCreateDto;
import ru.practicum.shareit.user.dto.request.create.UserRequestCreateDtoMapper;
import ru.practicum.shareit.user.dto.request.update.UserRequestUpdateDto;
import ru.practicum.shareit.user.dto.request.update.UserRequestUpdateDtoMapper;
import ru.practicum.shareit.user.dto.response.UserResponseDto;
import ru.practicum.shareit.user.dto.response.UserResponseDtoMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserRequestCreateDtoMapper userRequestCreateDtoMapper;
    private final UserRequestUpdateDtoMapper userRequestUpdateDtoMapper;
    private final UserResponseDtoMapper userResponseDtoMapper;

    @Override
    public UserResponseDto create(UserRequestCreateDto dto) {
        User newUser = userRequestCreateDtoMapper.toEntity(dto);

        return userResponseDtoMapper.fromEntity(userRepository.create(newUser));
    }

    @Override
    public UserResponseDto findById(long id) {
        return userResponseDtoMapper.fromEntity(userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Пользователь с id=%d не найден".formatted(id))));
    }

    @Override
    public UserResponseDto update(UserRequestUpdateDto dto) {
        User foundUser = userRepository.findById(dto.getId())
                .orElseThrow(() -> new NotFoundException("Пользователь с id=%d не найден".formatted(dto.getId())));

        User updatedUser = userRequestUpdateDtoMapper.returnUpdatedFromDtoEntity(foundUser, dto);

        return userResponseDtoMapper.fromEntity(userRepository.update(updatedUser));
    }

    @Override
    public void delete(long id) {
        userRepository.delete(id);
    }
}
