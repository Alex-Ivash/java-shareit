package ru.practicum.shareit.item.service;

import ru.practicum.shareit.BaseService;
import ru.practicum.shareit.item.dto.request.create.ItemRequestCreateDto;
import ru.practicum.shareit.item.dto.request.update.ItemRequestUpdateDto;
import ru.practicum.shareit.item.dto.response.ItemFromUserDto;
import ru.practicum.shareit.item.dto.response.ItemResponseDto;

import java.util.List;

public interface ItemService extends BaseService<ItemRequestCreateDto, ItemRequestUpdateDto, ItemResponseDto> {
    List<ItemFromUserDto> findAllItemsFromUser(long userId);

    List<ItemResponseDto> search(String text);
}
