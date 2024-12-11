package ru.practicum.shareit.user.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserResponseDto {
    private Long id;
    private String email;
    private String name;
}
