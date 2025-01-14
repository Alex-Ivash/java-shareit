package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.PermissionDeniedException;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.util.Collections;
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

        User owner = userRepository.findById(ownerId)
                .orElseThrow(() -> new NotFoundException("Пользователь с id=%d не найден".formatted(ownerId)));

        Item createdItem = itemRepository.save(itemDtoMapper.toEntity(dto, owner));

        return itemDtoMapper.toItemDto(createdItem);
    }

    @Override
    public ItemDto findById(long id) {
        return itemDtoMapper.toItemDto(itemRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Вещь с id=%d не найдена".formatted(id))));
    }

    @Override
    public ItemDto update(ItemUpdateDto dto) {
        Item foundItem = itemRepository.findById(dto.getId())
                .orElseThrow(() -> new NotFoundException("Вещь с id=%d не найдена".formatted(dto.getId())));

        if (!Objects.equals(dto.getCurrentUserId(), foundItem.getOwner().getId())) {
            throw new PermissionDeniedException("Изменение параметров вещи доступно только владельцу вещи");
        }

        itemDtoMapper.updateEntityFromDto(foundItem, dto);

        return itemDtoMapper.toItemDto(itemRepository.save(foundItem));
    }

    @Override
    public void delete(long id) {
        itemRepository.deleteById(id);
    }

    @Override
    public List<ItemShortDto> findAllItemsFromUser(long userId) {
        return itemRepository.findAllByOwner_id(userId);

//        return items.stream()
//                .map(itemDtoMapper::toItemShortDto)
//                .toList();
    }

    @Override
    public List<ItemDto> search(String text) {
        if (text.isBlank()) {
            return Collections.emptyList();
        }

        return itemRepository.search(text);

//        return items.stream()
//                .map(itemDtoMapper::toItemDto)
//                .toList();
    }
}
