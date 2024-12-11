package ru.practicum.shareit;

public interface BaseUpdateDtoMapper<D, E> extends BaseDtoMapper<D, E> {
    E returnUpdatedFromDtoEntity(E entity, D dto);
}
