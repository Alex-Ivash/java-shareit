package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.PermissionDeniedException;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final ItemDtoMapper itemDtoMapper;

    @Override
    public ItemDto create(ItemCreateDto dto) {
        long ownerId = dto.getOwnerId();

        userRepository.findById(ownerId)
                .orElseThrow(() -> new NotFoundException("Пользователь с id=%d не найден".formatted(ownerId)));

        Item createdItem = itemRepository.create(itemDtoMapper.toEntity(dto));

        return itemDtoMapper.toItemDto(createdItem);
    }

    @Override
    public ItemDto findById(long id) {
        return itemDtoMapper.toItemDto(itemRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Вещ с id=%d не найдена".formatted(id))));
    }

    @Override
    public ItemDto update(ItemUpdateDto dto) {
        Item foundItem = itemRepository.findById(dto.getId())
                .orElseThrow(() -> new NotFoundException("Вещ с id=%d не найдена".formatted(dto.getId())));

        if (!Objects.equals(dto.getCurrentUser(), foundItem.getOwnerId())) {
            throw new PermissionDeniedException("Изменение параметров вещи доступно только владельцу вещи");
        }

        Item updatedItem = itemDtoMapper.returnUpdatedEntityFromDto(foundItem, dto);

        return itemDtoMapper.toItemDto(itemRepository.update(updatedItem));
    }

    @Override
    public void delete(long id) {
        itemRepository.delete(id);
    }

    @Override
    public List<ItemShortDto> findAllItemsFromUser(long userId) {
        List<Item> items = itemRepository.findAllItemsFromUser(userId);

        return items.stream()
                .map(itemDtoMapper::toItemShortDto)
                .toList();
    }

    @Override
    public List<ItemDto> search(String text) {
        List<Item> items = itemRepository.search(text);

        return items.stream()
                .map(itemDtoMapper::toItemDto)
                .toList();
    }
}
