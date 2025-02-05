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
        return itemDtoMapper.toItemDto(
                itemRepository.save(
                        itemDtoMapper.toEntity(dto, findUser(dto.getOwnerId()))
                )
        );
    }

    @Override
    public CommentDto createComment(CommentCreateDto dto) {
        User author = findUser(dto.getAuthorId());
        Item item = findItem(dto.getItemId());

        if (!bookingRepository.existsByBookerAndItemAndStatusAndEndLessThan(author, item, BookingStatus.APPROVED, LocalDateTime.now())) {
            throw new EntityAvailableException("Комментарий к вещи может оставить только пользователь, бравший её в аренду");
        }

        return commentDtoMapper.toCommentDto(commentRepository.save(commentDtoMapper.toEntity(dto, item, author)));
    }

    @Override
    public ItemExtendedDto findById(long id) {
        return itemDtoMapper.toItemExtendedDto(findItem(id), commentRepository.findAllByItem_id(id));
    }

    @Override
    public ItemDto update(ItemUpdateDto dto) {
        Item foundItem = findItem(dto.getId());

        if (!Objects.equals(dto.getCurrentUserId(), foundItem.getOwner().getId())) {
            throw new PermissionDeniedException("Изменение параметров вещи доступно только владельцу вещи");
        }

        return itemDtoMapper.toItemDto(itemRepository.save(itemDtoMapper.getUpdatedEntityFromDto(foundItem, dto)));
    }

    @Override
    public void delete(long id) {
        itemRepository.deleteById(id);
    }

    @Override
    public List<ItemInfoDto> findAllItemsFromOwner(long ownerId) {
        List<Item> foundItems = itemRepository.findAllByOwner_Id(ownerId);
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

    private User findUser(long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь с id=%d не найден".formatted(userId)));
    }

    private Item findItem(long itemId) {
        return itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Вещь с id=%d не найдена".formatted(itemId)));
    }
}
