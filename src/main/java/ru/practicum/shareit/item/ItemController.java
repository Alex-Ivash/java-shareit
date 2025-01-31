package ru.practicum.shareit.item;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.*;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping(path = "/items")
@Validated
public class ItemController {
    private final ItemService itemService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ItemDto createItem(
            @RequestBody @Valid ItemCreateDto itemCreateDto,
            @RequestHeader("X-Sharer-User-Id") @Positive long userId
    ) {
        itemCreateDto.setOwnerId(userId);
        log.info("Создание новой вещи {} пользователем с id={}...", itemCreateDto, userId);
        ItemDto newItem = itemService.createItem(itemCreateDto);
        log.info("Вещь создана => {}.", newItem);

        return newItem;
    }

    @GetMapping("/{itemId}")
    @ResponseStatus(HttpStatus.OK)
    public ItemExtendedDto findById(@PathVariable @Positive long itemId) {
        log.info("Поиск вещи c id={}...", itemId);
        ItemExtendedDto foundItem = itemService.findById(itemId);
        log.info("Вещь найдена => {}.", foundItem);

        return foundItem;
    }

    @GetMapping("/search")
    @ResponseStatus(HttpStatus.OK)
    public List<ItemDto> search(@RequestParam String text) {
        log.info("Поиск вещей по запросу '{}'...", text);
        List<ItemDto> foundItems = itemService.search(text);
        log.info("Найдены вещи => {}.", foundItems);

        return foundItems;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ItemInfoDto> findAllItemsFromOwner(@RequestHeader("X-Sharer-User-Id") @Positive long userId) {
        log.info("Поиск всех вещей пользователя c id={}...", userId);
        List<ItemInfoDto> foundItems = itemService.findAllItemsFromOwner(userId);
        log.info("Вещи пользователя с id={} найдены => {}.", userId, foundItems);

        return foundItems;
    }

    @PatchMapping("/{itemId}")
    @ResponseStatus(HttpStatus.OK)
    public ItemDto update(
            @RequestBody @Valid ItemUpdateDto itemUpdateDto,
            @PathVariable @Positive long itemId,
            @RequestHeader("X-Sharer-User-Id") @Positive long userId
    ) {
        itemUpdateDto.setId(itemId);
        itemUpdateDto.setCurrentUserId(userId);
        log.info("Обновление данных {} вещи с id={} пользователем с id={}...", itemUpdateDto, itemId, userId);
        ItemDto updatedItem = itemService.update(itemUpdateDto);
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

    @PostMapping("/{itemId}/comment")
    @ResponseStatus(HttpStatus.CREATED)
    public CommentDto createComment(
            @RequestBody @Valid CommentCreateDto commentCreateDto,
            @RequestHeader("X-Sharer-User-Id") @Positive long userId,
            @PathVariable @Positive long itemId
    ) {
        commentCreateDto.setAuthorId(userId);
        commentCreateDto.setItemId(itemId);
        log.info("Создание нового комментария {} пользователем с id={}...", commentCreateDto, userId);
        CommentDto newComment = itemService.createComment(commentCreateDto);
        log.info("Комментарий создан => {}.", newComment);

        return newComment;
    }
}
