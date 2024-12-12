package ru.practicum.shareit.item.repository;

import ru.practicum.shareit.BaseRepository;
import ru.practicum.shareit.item.Item;

import java.util.List;

public interface ItemRepository extends BaseRepository<Item> {
    List<Item> search(String text);

    List<Item> findAllItemsFromUser(long userId);
}
