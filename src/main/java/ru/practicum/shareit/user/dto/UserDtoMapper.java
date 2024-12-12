package ru.practicum.shareit.user.dto;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.user.User;

@Component
public class UserDtoMapper {
    public UserDto toUserDto(User user) {
        return new UserDto(
                user.getId(),
                user.getEmail(),
                user.getName()
        );
    }

    public User toEntity(UserUpdateDto dto) {
        User user = new User();
        user.setId(dto.getId());
        user.setEmail(dto.getEmail());
        user.setName(dto.getName());

        return user;
    }

    public User toEntity(UserCreateDto dto) {
        User user = new User();
        user.setEmail(dto.getEmail());
        user.setName(dto.getName());

        return user;
    }

    public User returnUpdatedEntityFromDto(User entity, UserUpdateDto dto) {
        User updatedEntity = this.toEntity(dto);

        if (updatedEntity.getName() == null) {
            updatedEntity.setName(entity.getName());
        }

        if (updatedEntity.getEmail() == null) {
            updatedEntity.setEmail((entity.getEmail()));
        }

        return updatedEntity;
    }
}
