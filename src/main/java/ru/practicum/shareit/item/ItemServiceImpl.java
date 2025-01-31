package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.booking.dto.BookingShortDto;
import ru.practicum.shareit.exception.EntityAvailableException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.PermissionDeniedException;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final CommentDtoMapper commentDtoMapper;
    private final ItemDtoMapper itemDtoMapper;

    @Override
    public ItemDto createItem(ItemCreateDto dto) {
        long ownerId = dto.getOwnerId();

        User owner = userRepository.findById(ownerId)
                .orElseThrow(() -> new NotFoundException("Пользователь с id=%d не найден".formatted(ownerId)));

        Item createdItem = itemRepository.save(itemDtoMapper.toEntity(dto, owner));

        return itemDtoMapper.toItemDto(createdItem);
    }

    @Override
    public CommentDto createComment(CommentCreateDto dto) {
        long authorId = dto.getAuthorId();
        long itemId = dto.getItemId();

        User author = userRepository.findById(authorId)
                .orElseThrow(() -> new NotFoundException("Пользователь с id=%d не найден".formatted(authorId)));
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Вещь с id=%d не найдена".formatted(itemId)));

        if (!bookingRepository.existsByBookerAndItemAndStatusAndEndLessThan(author, item, BookingStatus.APPROVED, LocalDateTime.now())) {
            throw new EntityAvailableException("Комментарий к вещи может оставить только пользователь, бравший её в аренду");
        }

        Comment createdComment = commentRepository.save(commentDtoMapper.toEntity(dto, item, author));

        return commentDtoMapper.toCommentDto(createdComment);
    }

    /*
        В тестах в этом кейсе зачем-то нужны даты последнего и следующего бронирования.
        Но при этом ожидается, что они оба null при том, что ожидается, что будет коммент по этой вещи, те было бронирование и дата последнего в таком кейсе по любому будет.
        В общем, добавил бесполезные 2 поля с датами в DTO, чтобы прийти тесты API
     */
    @Override
    public ItemExtendedDto findById(long id) {
        Item foundItem = itemRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Вещь с id=%d не найдена".formatted(id)));
        List<CommentDto> comments = commentRepository.findAllByItem_id(id);

        return itemDtoMapper.toItemExtendedDto(foundItem, comments);
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
    public List<ItemInfoDto> findAllItemsFromOwner(long ownerId) {
        List<Item> foundItems = itemRepository.findAllByOwner_id(ownerId);
        List<Comment> comments = commentRepository.findAllByItemIn(foundItems);
        List<BookingShortDto> bookingLastAndPastDates = bookingRepository.findLastAndNextBookingDatesFromItems(LocalDateTime.now(), foundItems);

        Map<Long, BookingShortDto> bookingLastAndPastDatesMap = new HashMap<>();
        Map<Long, List<CommentDto>> commentsMap = new HashMap<>();

        bookingLastAndPastDates.forEach(b -> bookingLastAndPastDatesMap.put(b.getItemId(), b));

        comments.forEach(c -> commentsMap.computeIfAbsent(
                        c.getItem().getId(),
                        k -> new ArrayList<>())
                .add(commentDtoMapper.toCommentDto(c))
        );

        return foundItems.stream()
                .map(item -> itemDtoMapper.toItemInfoDto(
                        item, commentsMap.get(item.getId()), bookingLastAndPastDatesMap.get(item.getId())))
                .toList();
    }

    @Override
    public List<ItemDto> search(String text) {
        if (text.isBlank()) {
            return Collections.emptyList();
        }

        return itemRepository.search(text);
    }
}
