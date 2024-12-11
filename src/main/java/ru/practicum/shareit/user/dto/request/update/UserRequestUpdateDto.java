package ru.practicum.shareit.user.dto.request.update;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRequestUpdateDto {
    private Long id;

    @Email
    private String email;

    @Size(max = 255)
    private String name;
}
