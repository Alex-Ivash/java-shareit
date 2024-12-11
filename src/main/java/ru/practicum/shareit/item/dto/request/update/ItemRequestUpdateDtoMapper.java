package ru.practicum.shareit.item.dto.request.update;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.BaseUpdateDtoMapper;
import ru.practicum.shareit.item.model.Item;

@Component
public class ItemRequestUpdateDtoMapper implements BaseUpdateDtoMapper<ItemRequestUpdateDto, Item> {
    @Override
    public ItemRequestUpdateDto fromEntity(Item entity) {
        return new ItemRequestUpdateDto(
                entity.getId(),
                entity.getName(),
                entity.getDescription(),
                entity.getAvailable(),
                entity.getOwnerId()
        );
    }

    @Override
    public Item toEntity(ItemRequestUpdateDto dto) {
        Item item = new Item();
        item.setId(dto.getId());
        item.setName(dto.getName());
        item.setDescription(dto.getDescription());
        item.setAvailable(dto.getAvailable());

        return item;
    }

    @Override
    public Item returnUpdatedFromDtoEntity(Item entity, ItemRequestUpdateDto dto) {
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
