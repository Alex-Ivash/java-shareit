package ru.practicum.shareit.item.dto.request.update;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemRequestUpdateDto {
    private Long id;

    @Size(max = 255)
    private String name;

    private String description;

    private Boolean available;

    private Long currentUserId;
}
