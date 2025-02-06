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
        User foundUser = findUser(dto.getBookerId()); // тесты требуют, чтобы сначала проверялось существование
        Item foundItem = findItem(dto.getItemId());

        if (!foundItem.getAvailable()) {
            throw new EntityAvailableException("Вещь с id=%d недоступна для бронирования".formatted(foundItem.getId()));
        }

        return bookingDtoMapper.toBookingDto(
                bookingRepository.save(
                        bookingDtoMapper.toEntity(
                                dto,
                                foundItem,
                                foundUser
                        )
                )
        );
    }

    @Override
    public BookingDto findById(long bookingId, long userId) {
        findUser(userId);

        Booking foundBooking = findBooking(bookingId);

        if (!(foundBooking.getItem().getOwner().getId().equals(userId)
                || foundBooking.getBooker().getId().equals(userId))) {
            throw new PermissionDeniedException("Просмотр бронирования вещи доступен только автору бронирования или владельцу вещи");
        }

        return bookingDtoMapper.toBookingDto(foundBooking);
    }

    @Override
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public BookingDto changeApprovedStatus(long bookingId, long userId, boolean status) {
        Booking booking = findBooking(bookingId);

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
        findUser(userId);

        LocalDateTime now = LocalDateTime.now();

        List<Booking> foundBookings = switch (state) {
            case ALL -> bookingRepository.findAllByBooker_idOrderByStartDesc(userId);
            case CURRENT -> bookingRepository.findAllByBooker_IdAndEndGreaterThanEqualOrderByStartDesc(userId, now);
            case PAST -> bookingRepository.findAllByBooker_IdAndEndLessThanOrderByStartDesc(userId, now);
            case FUTURE -> bookingRepository.findAllByBooker_IdAndStartGreaterThanOrderByStartDesc(userId, now);
            case WAITING, REJECTED -> bookingRepository.findAllByBooker_IdAndStatusOrderByStartDesc(userId, state);
        };

        return foundBookings.stream()
                .map(bookingDtoMapper::toBookingDto)
                .toList();
    }

    @Override
    public List<BookingDto> findAllBookingsForOwnersItems(long userId, BookingRequestState state) {
        findUser(userId);

        LocalDateTime now = LocalDateTime.now();

        List<Booking> foundBookings = switch (state) {
            case ALL -> bookingRepository.findAllByItem_Owner_IdOrderByStartDesc(userId);
            case CURRENT -> bookingRepository.findAllByItem_Owner_IdAndEndGreaterThanEqualOrderByStartDesc(userId, now);
            case PAST -> bookingRepository.findAllByItem_Owner_IdAndEndLessThanOrderByStartDesc(userId, now);
            case FUTURE -> bookingRepository.findAllByItem_Owner_IdAndStartGreaterThanOrderByStartDesc(userId, now);
            case WAITING, REJECTED -> bookingRepository.findAllByItem_Owner_IdAndStatusOrderByStartDesc(userId, state);
        };

        return foundBookings.stream()
                .map(bookingDtoMapper::toBookingDto)
                .toList();
    }

    private User findUser(long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь с id=%d не найден".formatted(userId)));
    }

    private Booking findBooking(long bookingId) {
        return bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException("Бронирование с id=%d не найдено".formatted(bookingId)));
    }

    private Item findItem(long itemId) {
        return itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Вещь с id=%d не найдена".formatted(itemId)));
    }
}
