package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.booking.dto.BookingDto;

import java.util.List;

public interface BookingService {
    BookingDto create(BookingCreateDto dto);

    BookingDto findById(long id, long userId);

    BookingDto changeApprovedStatus(long bookingId, long userId, boolean status);

    List<BookingDto> findAllBookingsFromUser(long userId, BookingRequestState state);

    List<BookingDto> findAllBookingsForOwnersItems(long userId, BookingRequestState state);
}
