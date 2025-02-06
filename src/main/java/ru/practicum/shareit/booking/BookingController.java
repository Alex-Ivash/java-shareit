package ru.practicum.shareit.booking;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.booking.dto.BookingDto;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping(path = "/bookings")
@Validated
public class BookingController {
    private final BookingService bookingService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<BookingDto> findAllBookingsFromUser(
            @RequestHeader("X-Sharer-User-Id") @Positive long userId,
            @RequestParam(required = false, defaultValue = "ALL") BookingRequestState state
    ) {
        log.info("Поиск всех бронирований пользователя c id={} со стейтом {}...", userId, state);
        List<BookingDto> foundBookings = bookingService.findAllBookingsFromUser(userId, state);
        log.info("Бронирования пользователя с id={} со стейтом {} найдены => {}.", userId, state, foundBookings);

        return foundBookings;
    }

    @GetMapping("/owner")
    @ResponseStatus(HttpStatus.OK)
    public List<BookingDto> findAllBookingsForOwnersItems(
            @RequestHeader("X-Sharer-User-Id") @Positive long userId,
            @RequestParam(required = false, defaultValue = "ALL") BookingRequestState state
    ) {
        log.info("Поиск всех бронирований вещей во владении у пользователя c id={} со стейтом {}...", userId, state);
        List<BookingDto> foundBookings = bookingService.findAllBookingsForOwnersItems(userId, state);
        log.info("Бронирования для вещей во владении у пользователя с id={} со стейтом {} найдены => {}.", userId, state, foundBookings);

        return foundBookings;
    }

    @GetMapping("/{bookingId}")
    @ResponseStatus(HttpStatus.OK)
    public BookingDto findById(
            @RequestHeader("X-Sharer-User-Id") @Positive long userId,
            @PathVariable @Positive long bookingId
    ) {
        log.info("Поиск бронирования c id={}...", bookingId);
        BookingDto foundBooking = bookingService.findById(bookingId, userId);
        log.info("Бронирование найдено => {}.", foundBooking);

        return foundBooking;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BookingDto create(
            @RequestHeader("X-Sharer-User-Id") @Positive long userId,
            @RequestBody @Valid BookingCreateDto dto
    ) {
        log.info("Создание нового бронирования {}...", dto);
        dto.setBookerId(userId);
        BookingDto newBooking = bookingService.create(dto);
        log.info("Бронирование создано => {}.", newBooking);

        return newBooking;
    }

    @PatchMapping("/{bookingId}")
    @ResponseStatus(HttpStatus.OK)
    public BookingDto changeApprovedStatus(
            @RequestHeader("X-Sharer-User-Id") @Positive long userId,
            @PathVariable long bookingId,
            @RequestParam boolean approved
    ) {
        log.info("Изменение статуса подтверждения бронирования c id={} на '{}' пользователем с id={}...", bookingId, approved, userId);
        BookingDto booking = bookingService.changeApprovedStatus(bookingId, userId, approved);
        log.info("Статус подтверждения бронирования {} изменен на {}.", booking, booking.getStatus());

        return booking;
    }
}
