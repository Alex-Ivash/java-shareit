package ru.practicum.shareit.item.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ItemFromUserDto {
    private String name;
    private String description;
}
