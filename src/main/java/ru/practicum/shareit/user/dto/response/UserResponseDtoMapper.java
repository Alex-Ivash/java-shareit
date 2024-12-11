package ru.practicum.shareit.user.dto.response;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.BaseDtoMapper;

@Component
public class UserResponseDtoMapper implements BaseDtoMapper<UserResponseDto, User> {
    @Override
    public UserResponseDto fromEntity(User entity) {
        return new UserResponseDto(entity.getId(), entity.getEmail(), entity.getName());
    }

    @Override
    public User toEntity(UserResponseDto dto) {
        User user = new User();
        user.setId(dto.getId());
        user.setEmail(dto.getEmail());
        user.setName(dto.getName());

        return user;
    }
}
