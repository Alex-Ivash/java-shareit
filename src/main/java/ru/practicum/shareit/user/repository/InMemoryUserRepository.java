package ru.practicum.shareit.user.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exception.ConflictException;
import ru.practicum.shareit.user.model.User;

import java.util.*;

@Repository
@RequiredArgsConstructor
public class InMemoryUserRepository implements UserRepository {
    private final Map<Long, User> users = new HashMap<>();
    private final Set<String> registeredEmails = new HashSet<>();
    private long idHolder = 0;

    @Override
    public User create(User newEntity) {
        checkEmailForUniqueness(newEntity.getEmail());
        newEntity.setId(getNextId());
        users.put(newEntity.getId(), newEntity);

        return newEntity;
    }

    @Override
    public Optional<User> findById(long id) {
        return Optional.ofNullable(users.get(id));
    }

    @Override
    public List<User> findAll() {
        return new ArrayList<>(users.values());
    }

    @Override
    public User update(User updatedEntity) {
        return users.computeIfPresent(updatedEntity.getId(), (id, oldUser) -> {
            registeredEmails.remove(oldUser.getEmail());
            checkEmailForUniqueness(updatedEntity.getEmail());

            return updatedEntity;
        });
    }

    @Override
    public void delete(long id) {
        //TODO реализовать метод удаления вещи в будущих спринтах
    }

    private long getNextId() {
        return ++idHolder;
    }

    private void checkEmailForUniqueness(String email) {
        if (registeredEmails.contains(email)) {
            throw new ConflictException("Пользователь с email=%s уже существует".formatted(email));
        }

        registeredEmails.add(email);
    }
}
