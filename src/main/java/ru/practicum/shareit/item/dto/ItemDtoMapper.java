package ru.practicum.shareit.item.dto;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.user.User;

@Component
public class ItemDtoMapper {
    public ItemDto toItemDto(Item item) {
        return new ItemDto(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getAvailable(),
                1L
//                item.getOwnerId()
        );
    }

    public ItemShortDto toItemShortDto(Item item) {
        return new ItemShortDto(
                item.getName(),
                item.getDescription()
        );
    }

    public Item toEntity(ItemCreateDto dto, User owner) {
        Item item = new Item();
        item.setName(dto.getName());
        item.setDescription(dto.getDescription());
        item.setAvailable(dto.getAvailable());
        item.setOwner(owner);

        return item;
    }

    public void updateEntityFromDto(Item entity, ItemUpdateDto dto) {
        if (dto.getName() != null) {
            entity.setName(dto.getName());
        }

        if (dto.getDescription() != null) {
            entity.setDescription((dto.getDescription()));
        }

        if (dto.getAvailable() != null) {
            entity.setAvailable(dto.getAvailable());
        }
    }
}
