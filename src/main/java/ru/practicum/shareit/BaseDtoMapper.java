package ru.practicum.shareit;

public interface BaseDtoMapper<D, E> {
    D fromEntity(E entity);
    E toEntity(D dto);
}
