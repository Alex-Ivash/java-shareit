package ru.practicum.shareit;

public interface BaseService<C, U, R> {
    R create(C dto);

    R findById(long id);

    R update(U dto);

    void delete(long id);
}
