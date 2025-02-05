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
        return userDtoMapper.toUserDto(findUser(id));
    }

    @Override
    public UserDto update(UserUpdateDto dto) {
        return userDtoMapper.toUserDto(
                userRepository.save(
                        userDtoMapper.getUpdatedEntityFromDto(
                                findUser(dto.getId()),
                                dto
                        )
                )
        );
    }

    @Override
    public void delete(long id) {
        userRepository.deleteById(id);
    }

    private User findUser(long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь с id=%d не найден".formatted(userId)));
    }
}
