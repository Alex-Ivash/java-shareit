package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.ItemCreateDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemShortDto;
import ru.practicum.shareit.item.dto.ItemUpdateDto;

import java.util.List;

public interface ItemService {
    ItemDto create(ItemCreateDto dto);

    ItemDto findById(long id);

    ItemDto update(ItemUpdateDto dto);

    void delete(long id);

    List<ItemShortDto> findAllItemsFromUser(long userId);

    List<ItemDto> search(String text);
}
