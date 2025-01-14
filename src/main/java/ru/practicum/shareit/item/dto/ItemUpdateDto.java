package ru.practicum.shareit.item.dto;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ItemUpdateDto {
    private Long id;

    @Size(max = 255)
    private String name;

    private String description;

    private Boolean available;

    private Long currentUserId;
}
