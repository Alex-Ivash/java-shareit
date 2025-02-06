package ru.practicum.shareit.item;

import jakarta.persistence.*;
import lombok.*;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;

@Entity
@Table(name = "comments")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = {"id"})
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    public String text;

    @ManyToOne
    @JoinColumn(nullable = false)
    public Item item;

    @ManyToOne
    @JoinColumn(nullable = false)
    public User author;

    public LocalDateTime created = LocalDateTime.now();
}
