package ru.practicum.shareit.exception;

public class EntityAvailableException extends RuntimeException {
    public EntityAvailableException(String message) {
        super(message);
    }
}
