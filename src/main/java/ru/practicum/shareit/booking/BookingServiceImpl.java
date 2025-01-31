package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoMapper;
import ru.practicum.shareit.exception.BookingStatusConflictException;
import ru.practicum.shareit.exception.EntityAvailableException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.PermissionDeniedException;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final BookingDtoMapper bookingDtoMapper;

    @Override
    public BookingDto create(BookingCreateDto dto) {
        long bookerId = dto.getBookerId();
        long bookingItemId = dto.getItemId();

        User booker = userRepository.findById(bookerId)
                .orElseThrow(() -> new NotFoundException("Пользователь с id=%d не найден".formatted(bookerId)));
        Item item = itemRepository.findById(bookingItemId)
                .orElseThrow(() -> new NotFoundException("Вещь с id=%d не найдена".formatted(bookingItemId)));

        if (!item.getAvailable()) {
            throw new EntityAvailableException("Вещь с id=%d недоступна для бронирования".formatted(item.getId()));
        }

        Booking createdBooking = bookingRepository.save(bookingDtoMapper.toEntity(dto, item, booker));

        return bookingDtoMapper.toBookingDto(createdBooking);
    }

    @Override
    public BookingDto findById(long id, long userId) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("Пользователь с id=%d не найден".formatted(userId));
        }

        Booking foundBooking = bookingRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Бронирование с id=%d не найдено".formatted(id)));

        Long bookingItemOwnerId = foundBooking.getItem()
                .getOwner()
                .getId();
        Long bookerId = foundBooking.getBooker().getId();

        if (!(bookingItemOwnerId.equals(userId) || bookerId.equals(userId))) {
            throw new PermissionDeniedException("Просмотр бронирования вещи доступен только автору бронирования или владельцу вещи");
        }

        return bookingDtoMapper.toBookingDto(foundBooking);
    }

    @Override
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public BookingDto changeApprovedStatus(long bookingId, long userId, boolean status) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException("Бронирование с id=%d не найдено".formatted(bookingId)));

        if (!booking.getItem().getOwner().getId().equals(userId)) {
            throw new PermissionDeniedException("Подтверждение/отклонение бронирования может быть выполнено только владельцем бронируемой вещи");
        }

        if (booking.getStatus() == BookingStatus.CANCELED) {
            throw new BookingStatusConflictException("Бронирование было отменено создателем");
        }

        booking.setStatus(status ? BookingStatus.APPROVED : BookingStatus.REJECTED);

        return bookingDtoMapper.toBookingDto(bookingRepository.save(booking));
    }

    @Override
    public List<BookingDto> findAllBookingsFromUser(long userId, BookingRequestState state) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("Пользователь с id=%d не найден".formatted(userId));
        }

        LocalDateTime now = LocalDateTime.now();

        return switch (state) {
            case ALL -> bookingRepository.findAllByBooker_idOrderByStartDesc(userId);
            case CURRENT -> bookingRepository.findAllByBooker_idAndEndGreaterThanEqualOrderByStartDesc(userId, now);
            case PAST -> bookingRepository.findAllByBooker_idAndEndLessThanOrderByStartDesc(userId, now);
            case FUTURE -> bookingRepository.findAllByBooker_idAndStartGreaterThanOrderByStartDesc(userId, now);
            case WAITING, REJECTED -> bookingRepository.findAllByBooker_idAndStatusOrderByStartDesc(userId, state);
        };
    }

    @Override
    public List<BookingDto> findAllBookingsForOwnersItems(long userId, BookingRequestState state) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("Пользователь с id=%d не найден".formatted(userId));
        }

        LocalDateTime now = LocalDateTime.now();

        return switch (state) {
            case ALL -> bookingRepository.findAllByItem_owner_idOrderByStartDesc(userId);
            case CURRENT -> bookingRepository.findAllByItem_owner_idAndEndGreaterThanEqualOrderByStartDesc(userId, now);
            case PAST -> bookingRepository.findAllByItem_owner_idAndEndLessThanOrderByStartDesc(userId, now);
            case FUTURE -> bookingRepository.findAllByItem_owner_idAndStartGreaterThanOrderByStartDesc(userId, now);
            case WAITING, REJECTED -> bookingRepository.findAllByItem_owner_idAndStatusOrderByStartDesc(userId, state);
        };
    }
}
