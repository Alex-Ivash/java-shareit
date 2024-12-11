package ru.practicum.shareit.item.dto.response;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.BaseDtoMapper;

@Component
public class ItemFromUserDtoMapper implements BaseDtoMapper<ItemFromUserDto, Item> {
    @Override
    public ItemFromUserDto fromEntity(Item entity) {
        return new ItemFromUserDto(
                entity.getName(),
                entity.getDescription()
        );
    }

    @Override
    public Item toEntity(ItemFromUserDto dto) {
        Item item = new Item();
        item.setName(dto.getName());
        item.setDescription(dto.getDescription());

        return null;
    }
}
