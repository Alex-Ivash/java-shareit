package ru.practicum.shareit.booking.dto;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.dto.ItemDtoMapper;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.dto.UserDtoMapper;

@Component
@RequiredArgsConstructor
public class BookingDtoMapper {
    private final ItemDtoMapper itemDtoMapper;
    private final UserDtoMapper userDtoMapper;

    public BookingDto toBookingDto(Booking booking) {
        return new BookingDto(
                booking.getId(),
                itemDtoMapper.toItemDto(booking.getItem()),
                userDtoMapper.toUserDto(booking.getBooker()),
                booking.getStart(),
                booking.getEnd(),
                booking.getStatus()
        );
    }

    public Booking toEntity(BookingCreateDto dto, Item item, User booker) {
        Booking booking = new Booking();

        booking.setItem(item);
        booking.setBooker(booker);
        booking.setStart(dto.getStart());
        booking.setEnd(dto.getEnd());
        booking.setStatus(BookingStatus.WAITING);

        return booking;
    }
}
