package ru.practicum.shareit.item.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.request.create.ItemRequestCreateDto;
import ru.practicum.shareit.item.dto.request.update.ItemRequestUpdateDto;
import ru.practicum.shareit.item.dto.response.ItemFromUserDto;
import ru.practicum.shareit.item.dto.response.ItemResponseDto;
import ru.practicum.shareit.item.service.ItemService;

import java.util.List;

/**
 * TODO Sprint add-controllers.
 */
@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping(path = "/items")
public class ItemController {
    private final ItemService itemService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ItemResponseDto create(
            @RequestBody @Valid ItemRequestCreateDto itemRequestCreateDto,
            @RequestHeader("X-Sharer-User-Id") @Positive long userId
    ) {
        log.info("Создание новой вещи {} пользователем с id={}...", itemRequestCreateDto, userId);
        itemRequestCreateDto.setOwnerId(userId);
        ItemResponseDto newItem = itemService.create(itemRequestCreateDto);
        log.info("Вещь создана => {}.", newItem);

        return newItem;
    }

    @GetMapping("/{itemId}")
    @ResponseStatus(HttpStatus.OK)
    public ItemResponseDto findById(@PathVariable @Positive long itemId) {
        log.info("Поиск вещи c id={}...", itemId);
        ItemResponseDto foundItem = itemService.findById(itemId);
        log.info("Вещь найдена => {}.", foundItem);

        return foundItem;
    }

    @GetMapping("/search")
    @ResponseStatus(HttpStatus.OK)
    public List<ItemResponseDto> search(@RequestParam String text) {
        log.info("Поиск вещей по запросу {}...", text);
        List<ItemResponseDto> foundItems = itemService.search(text);
        log.info("Найдены вещи => {}.", foundItems);

        return foundItems;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ItemFromUserDto> findAllItemsFromUser(@RequestHeader("X-Sharer-User-Id") @Positive long userId) {
        log.info("Поиск всех вещей пользователя c id={}...", userId);
        List<ItemFromUserDto> foundItems = itemService.findAllItemsFromUser(userId);
        log.info("Вещи пользователя с id={} найдены => {}.", userId, foundItems);

        return foundItems;
    }

    @PatchMapping("/{itemId}")
    @ResponseStatus(HttpStatus.OK)
    public ItemResponseDto update(
            @RequestBody @Valid ItemRequestUpdateDto itemRequestUpdateDto,
            @PathVariable @Positive long itemId,
            @RequestHeader("X-Sharer-User-Id") @Positive long userId
    ) {
        itemRequestUpdateDto.setId(itemId);
        itemRequestUpdateDto.setCurrentUserId(userId);
        log.info("Обновление данных {} вещи с id={} пользователем с id={}...", itemRequestUpdateDto, itemId, userId);
        ItemResponseDto updatedItem = itemService.update(itemRequestUpdateDto);
        log.info("Вещь обновлена => {}.", updatedItem);

        return updatedItem;
    }

    @DeleteMapping("/{itemId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable @Positive long itemId) {
        log.info("Удаление пользователя с id={}...", itemId);
        itemService.delete(itemId);
        log.info("Пользователь с id={} удален.", itemId);
    }
}
