package ru.practicum.shareit.booking;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.shareit.booking.dto.BookingShortDto;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findAllByBooker_idOrderByStartDesc(long bookerId);

    List<Booking> findAllByItem_Owner_IdOrderByStartDesc(long ownerId);


    List<Booking> findAllByBooker_IdAndStatusOrderByStartDesc(long bookerId, BookingRequestState state);

    List<Booking> findAllByItem_Owner_IdAndStatusOrderByStartDesc(long ownerId, BookingRequestState state);


    List<Booking> findAllByBooker_IdAndEndGreaterThanEqualOrderByStartDesc(long bookerId, LocalDateTime end);

    List<Booking> findAllByBooker_IdAndEndLessThanOrderByStartDesc(long bookerId, LocalDateTime end);

    List<Booking> findAllByBooker_IdAndStartGreaterThanOrderByStartDesc(long bookerId, LocalDateTime start);


    List<Booking> findAllByItem_Owner_IdAndEndGreaterThanEqualOrderByStartDesc(long ownerId, LocalDateTime end);

    List<Booking> findAllByItem_Owner_IdAndEndLessThanOrderByStartDesc(long ownerId, LocalDateTime end);

    List<Booking> findAllByItem_Owner_IdAndStartGreaterThanOrderByStartDesc(long ownerId, LocalDateTime start);


    @Query("""
            SELECT new ru.practicum.shareit.booking.dto.BookingShortDto(
                   b.item.id,
                   MAX(CASE WHEN b.start < :currentDate THEN b.start END) AS lastBooking,
                   MIN(CASE WHEN b.start > :currentDate THEN b.start END) AS nextBooking
            )
            FROM Booking b
            WHERE b.item IN (:items)
            GROUP BY b.item.id
            """)
    List<BookingShortDto> findLastAndNextBookingDatesFromItems(@Param("currentDate") LocalDateTime currentDate, @Param("items") List<Item> items);

    boolean existsByBookerAndItemAndStatusAndEndLessThan(User booker, Item item, BookingStatus status, LocalDateTime end);
}
