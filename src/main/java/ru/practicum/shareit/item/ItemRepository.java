package ru.practicum.shareit.item;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {
    @Query("""
            SELECT new ru.practicum.shareit.item.dto.ItemDto(it.id, it.name, it.description, it.available)
            FROM Item it
            WHERE it.available = true AND
                  (LOWER(it.name) LIKE LOWER(CONCAT('%', :text, '%')) OR
                   LOWER(it.description) LIKE LOWER(CONCAT('%', :text, '%')))
            """)
    List<ItemDto> search(@Param("text") String text);

    List<Item> findAllByOwner_Id(long userId);
}
