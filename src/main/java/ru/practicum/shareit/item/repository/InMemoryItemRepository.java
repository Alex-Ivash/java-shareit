package ru.practicum.shareit.item.repository;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.Item;

import java.util.*;

@Repository
public class InMemoryItemRepository implements ItemRepository {
    private final Map<Long, Item> items = new HashMap<>();
    private final Map<Long, List<Item>> usersToItems = new HashMap<>();
    private final Set<Item> availableItems = new HashSet<>();
    private long idHolder = 0;

    @Override
    public Item create(Item newEntity) {
        newEntity.setId(getNextId());
        items.put(newEntity.getId(), newEntity);
        usersToItems.computeIfAbsent(newEntity.getOwnerId(), k -> new ArrayList<>()).add(newEntity);

        if (newEntity.getAvailable()) {
            availableItems.add(newEntity);
        }

        return newEntity;
    }

    @Override
    public Optional<Item> findById(long id) {
        return Optional.ofNullable(items.get(id));
    }

    @Override
    public List<Item> findAll() {
        return new ArrayList<>(items.values());
    }

    @Override
    public Item update(Item updatedEntity) {
        if (updatedEntity.getAvailable()) {
            availableItems.add(updatedEntity);
        } else {
            availableItems.remove(updatedEntity);
        }

        return items.computeIfPresent(updatedEntity.getId(), (id, oldItem) -> updatedEntity);
    }

    @Override
    public void delete(long id) {
        //TODO реализовать метод удаления вещи в будущих спринтах
    }

    private long getNextId() {
        return ++idHolder;
    }

    @Override
    public List<Item> search(String text) {
        if (text.isBlank()) {
            return Collections.emptyList();
        }

        String pattern = "(?ui).*%s.*".formatted(text);

        return availableItems.stream()
                .filter(item -> item.getDescription().matches(pattern) || item.getName().matches(pattern))
                .toList();
    }

    @Override
    public List<Item> findAllItemsFromUser(long userId) {
        return usersToItems.get(userId);
    }
}
