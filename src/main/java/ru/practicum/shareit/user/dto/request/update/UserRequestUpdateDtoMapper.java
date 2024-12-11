package ru.practicum.shareit.user.dto.request.update;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.BaseUpdateDtoMapper;
import ru.practicum.shareit.user.model.User;

@Component
public class UserRequestUpdateDtoMapper implements BaseUpdateDtoMapper<UserRequestUpdateDto, User> {
    @Override
    public UserRequestUpdateDto fromEntity(User entity) {
        return new UserRequestUpdateDto(entity.getId(), entity.getEmail(), entity.getName());
    }

    @Override
    public User toEntity(UserRequestUpdateDto dto) {
        User user = new User();
        user.setId(dto.getId());
        user.setEmail(dto.getEmail());
        user.setName(dto.getName());

        return user;
    }

    @Override
    public User returnUpdatedFromDtoEntity(User entity, UserRequestUpdateDto dto) {
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
