package ru.practicum.shareit.item.dto.request.create;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.BaseDtoMapper;
import ru.practicum.shareit.item.model.Item;


@Component
public class ItemRequestCreateDtoMapper implements BaseDtoMapper<ItemRequestCreateDto, Item> {
    @Override
    public ItemRequestCreateDto fromEntity(Item entity) {
        return new ItemRequestCreateDto(entity.getName(), entity.getDescription(), entity.getAvailable(), entity.getOwnerId());
    }

    @Override
    public Item toEntity(ItemRequestCreateDto dto) {
        Item item = new Item();
        item.setName(dto.getName());
        item.setDescription(dto.getDescription());
        item.setAvailable(dto.getAvailable());
        item.setOwnerId(dto.getOwnerId());

        return item;
    }
}
