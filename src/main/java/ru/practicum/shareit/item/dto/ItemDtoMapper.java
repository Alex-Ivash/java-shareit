package ru.practicum.shareit.item.dto;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.dto.BookingShortDto;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.user.User;

import java.util.List;

@Component
public class ItemDtoMapper {
    public ItemDto toItemDto(Item item) {
        return new ItemDto(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getAvailable()
        );
    }

    public ItemExtendedDto toItemExtendedDto(Item item, List<CommentDto> comments) {
        return new ItemExtendedDto(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getAvailable(),
                null, // в сервисе описал это странное действие
                null, // в сервисе описал это странное действие
                comments
        );
    }

    public ItemInfoDto toItemInfoDto(Item item, List<CommentDto> comments, BookingShortDto bookingShortDto) {
        return new ItemInfoDto(
                item.getName(),
                item.getDescription(),
                bookingShortDto != null ? bookingShortDto.getLastBooking() : null,
                bookingShortDto != null ? bookingShortDto.getNextBooking() : null,
                comments
        );
    }

    public Item toEntity(ItemCreateDto dto, User owner) {
        Item item = new Item();

        item.setName(dto.getName());
        item.setDescription(dto.getDescription());
        item.setAvailable(dto.getAvailable());
        item.setOwner(owner);

        return item;
    }

    public void updateEntityFromDto(Item entity, ItemUpdateDto dto) {
        if (dto.getName() != null) {
            entity.setName(dto.getName());
        }

        if (dto.getDescription() != null) {
            entity.setDescription((dto.getDescription()));
        }

        if (dto.getAvailable() != null) {
            entity.setAvailable(dto.getAvailable());
        }
    }
}
