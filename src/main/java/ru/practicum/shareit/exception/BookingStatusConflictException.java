package ru.practicum.shareit.exception;

public class BookingStatusConflictException extends RuntimeException {
    public BookingStatusConflictException(String message) {
        super(message);
    }
}
