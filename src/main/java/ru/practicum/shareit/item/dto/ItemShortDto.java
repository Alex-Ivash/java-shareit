package ru.practicum.shareit.item.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ItemShortDto {
    @NotBlank
    @Size(max = 255)
    private String name;

    @NotBlank
    private String description;
}
