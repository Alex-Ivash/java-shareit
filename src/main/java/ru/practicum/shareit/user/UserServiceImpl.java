package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.dto.UserCreateDto;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserDtoMapper;
import ru.practicum.shareit.user.dto.UserUpdateDto;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserDtoMapper userDtoMapper;

    @Override
    public UserDto create(UserCreateDto dto) {
        User newUser = userDtoMapper.toEntity(dto);

        return userDtoMapper.toUserDto(userRepository.save(newUser));
    }

    @Override
    public UserDto findById(long id) {
        return userDtoMapper.toUserDto(userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Пользователь с id=%d не найден".formatted(id))));
    }

    @Override
    public UserDto update(UserUpdateDto dto) {
        User foundUser = userRepository.findById(dto.getId())
                .orElseThrow(() -> new NotFoundException("Пользователь с id=%d не найден".formatted(dto.getId())));

        userDtoMapper.updateEntityFromDto(foundUser, dto);

        return userDtoMapper.toUserDto(userRepository.save(foundUser));
    }

    @Override
    public void delete(long id) {
        userRepository.deleteById(id);
    }
}
