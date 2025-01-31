package ru.practicum.shareit.booking.dto;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class BookingCreateDto {
    @NotNull
    private Long itemId;

    private Long bookerId;

    @FutureOrPresent
    @NotNull
    private LocalDateTime start;

    @Future
    @NotNull
    private LocalDateTime end;

    @AssertTrue(message = "дата и время старта и окончания бронирования не могут быть одинаковыми")
    public boolean isDatesNotEqual() {
        if (start != null && end != null) {
            return !start.equals(end);
        }

        return true;
    }
}
