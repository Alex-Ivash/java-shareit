package ru.practicum.shareit.item.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ItemCreateDto {
    @NotBlank
    @Size(max = 255)
    private String name;

    @NotBlank
    private String description;

    @NotNull
    private Boolean available;

    private Long ownerId;
}
