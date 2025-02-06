package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.*;

import java.util.List;

public interface ItemService {
    ItemDto createItem(ItemCreateDto dto);

    CommentDto createComment(CommentCreateDto dto);

    ItemExtendedDto findById(long id);

    ItemDto update(ItemUpdateDto dto);

    void delete(long id);

    List<ItemInfoDto> findAllItemsFromOwner(long userId);

    List<ItemDto> search(String text);
}
