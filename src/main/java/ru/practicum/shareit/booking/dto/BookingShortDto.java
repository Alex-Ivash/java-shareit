package ru.practicum.shareit.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class BookingShortDto {
    private Long itemId;
    private LocalDateTime lastBooking;
    private LocalDateTime nextBooking;
}
