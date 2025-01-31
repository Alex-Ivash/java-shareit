package ru.practicum.shareit.item.dto;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.Comment;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.user.User;

@Component
public class CommentDtoMapper {
    public CommentDto toCommentDto(Comment comment) {
        return new CommentDto(
                comment.getId(),
                comment.getText(),
                comment.getAuthor().getName(),
                comment.getCreated()
        );
    }

    public Comment toEntity(CommentCreateDto dto, Item item, User author) {
        Comment comment = new Comment();

        comment.setText(dto.getText());
        comment.setItem(item);
        comment.setAuthor(author);

        return comment;
    }
}
