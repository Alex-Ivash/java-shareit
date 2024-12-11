package ru.practicum.shareit.item.dto.response;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.BaseDtoMapper;

@Component
public class ItemResponseDtoMapper implements BaseDtoMapper<ItemResponseDto, Item> {
    @Override
    public ItemResponseDto fromEntity(Item entity) {
        return new ItemResponseDto(
                entity.getId(),
                entity.getName(),
                entity.getDescription(),
                entity.getAvailable()
        );
    }

    @Override
    public Item toEntity(ItemResponseDto dto) {
        Item item = new Item();
        item.setId(dto.getId());
        item.setName(dto.getName());
        item.setDescription(dto.getDescription());
        item.setAvailable(dto.getAvailable());

        return item;
    }
}
