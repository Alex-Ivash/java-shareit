package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.PermissionDeniedException;
import ru.practicum.shareit.item.dto.request.create.ItemRequestCreateDto;
import ru.practicum.shareit.item.dto.request.create.ItemRequestCreateDtoMapper;
import ru.practicum.shareit.item.dto.request.update.ItemRequestUpdateDto;
import ru.practicum.shareit.item.dto.request.update.ItemRequestUpdateDtoMapper;
import ru.practicum.shareit.item.dto.response.ItemFromUserDto;
import ru.practicum.shareit.item.dto.response.ItemFromUserDtoMapper;
import ru.practicum.shareit.item.dto.response.ItemResponseDto;
import ru.practicum.shareit.item.dto.response.ItemResponseDtoMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final ItemRequestUpdateDtoMapper itemRequestUpdateDtoMapper;
    private final ItemRequestCreateDtoMapper itemRequestCreateDtoMapper;
    private final ItemResponseDtoMapper itemResponseDtoMapper;
    private final ItemFromUserDtoMapper itemFromUserDtoMapper;


    @Override
    public ItemResponseDto create(ItemRequestCreateDto itemRequestCreateDto) {
        long ownerId = itemRequestCreateDto.getOwnerId();

        userRepository.findById(ownerId)
                .orElseThrow(() -> new NotFoundException("Пользователь с id=%d не найден".formatted(ownerId)));

        Item createdItem = itemRepository.create(itemRequestCreateDtoMapper.toEntity(itemRequestCreateDto));

        return itemResponseDtoMapper.fromEntity(createdItem);
    }

    @Override
    public ItemResponseDto findById(long id) {
        return itemResponseDtoMapper.fromEntity(itemRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Вещ с id=%d не найдена".formatted(id))));
    }

    @Override
    public ItemResponseDto update(ItemRequestUpdateDto dto) {
        Item foundItem = itemRepository.findById(dto.getId())
                .orElseThrow(() -> new NotFoundException("Вещ с id=%d не найдена".formatted(dto.getId())));

        if (!Objects.equals(dto.getCurrentUserId(), foundItem.getOwnerId())) {
            throw new PermissionDeniedException("Изменение параметров вещи доступно только владельцу вещи");
        }

        Item updatedItem = itemRequestUpdateDtoMapper.returnUpdatedFromDtoEntity(foundItem, dto);

        return itemResponseDtoMapper.fromEntity(itemRepository.update(updatedItem));
    }

    @Override
    public void delete(long id) {
        itemRepository.delete(id);
    }

    @Override
    public List<ItemFromUserDto> findAllItemsFromUser(long userId) {
        List<Item> items = itemRepository.findAllItemsFromUser(userId);

        return items.stream()
                .map(itemFromUserDtoMapper::fromEntity)
                .toList();
    }

    @Override
    public List<ItemResponseDto> search(String text) {
        List<Item> items = itemRepository.search(text);

        return items.stream()
                .map(itemResponseDtoMapper::fromEntity)
                .toList();
    }
}
