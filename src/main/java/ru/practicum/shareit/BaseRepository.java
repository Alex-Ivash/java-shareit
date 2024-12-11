package ru.practicum.shareit;

import java.util.List;
import java.util.Optional;

public interface BaseRepository<T> {
    T create(T newEntity);

    Optional<T> findById(long id);

    List<T> findAll();

    T update(T updateEntity);

    void delete(long id);
}
