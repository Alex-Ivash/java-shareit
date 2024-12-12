package ru.practicum.shareit.item.dto;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.Item;

@Component
public class ItemDtoMapper {
    public ItemDto toItemDto(Item item) {
        return new ItemDto(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getAvailable(),
                item.getOwnerId()
        );
    }

    public ItemShortDto toItemShortDto(Item item) {
        return new ItemShortDto(
                item.getName(),
                item.getDescription()
        );
    }

    public Item toEntity(ItemCreateDto dto) {
        Item item = new Item();
        item.setName(dto.getName());
        item.setDescription(dto.getDescription());
        item.setAvailable(dto.getAvailable());
        item.setOwnerId(dto.getOwnerId());

        return item;
    }

    public Item toEntity(ItemUpdateDto dto) {
        Item item = new Item();
        item.setId(dto.getId());
        item.setName(dto.getName());
        item.setDescription(dto.getDescription());
        item.setAvailable(dto.getAvailable());
        item.setOwnerId(dto.getCurrentUser());

        return item;
    }

    public Item returnUpdatedEntityFromDto(Item entity, ItemUpdateDto dto) {
        Item updatedEntity = this.toEntity(dto);

        if (updatedEntity.getName() == null) {
            updatedEntity.setName(entity.getName());
        }

        if (updatedEntity.getDescription() == null) {
            updatedEntity.setDescription((entity.getDescription()));
        }

        if (updatedEntity.getAvailable() == null) {
            updatedEntity.setAvailable(entity.getAvailable());
        }

        return updatedEntity;
    }
}
