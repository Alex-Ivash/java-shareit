package ru.practicum.shareit.user.dto.request.create;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.BaseDtoMapper;
import ru.practicum.shareit.user.model.User;

@Component
public class UserRequestCreateDtoMapper implements BaseDtoMapper<UserRequestCreateDto, User> {
    @Override
    public UserRequestCreateDto fromEntity(User entity) {
        return new UserRequestCreateDto(entity.getEmail(), entity.getName());
    }

    @Override
    public User toEntity(UserRequestCreateDto dto) {
        User user = new User();
        user.setEmail(dto.getEmail());
        user.setName(dto.getName());

        return user;
    }
}
