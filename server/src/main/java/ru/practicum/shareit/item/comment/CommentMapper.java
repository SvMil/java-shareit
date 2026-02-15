package ru.practicum.shareit.item.comment;

public class CommentMapper {
    public static CommentDto toDto(Comment comment) {
        if (comment == null) {
            return null;
        }

        String authorName = "Unknown";

        if (comment.getAuthor() != null) {
            authorName = comment.getAuthor().getName();
        }

        return new CommentDto(
                comment.getId(),
                comment.getText(),
                authorName,
                comment.getCreated()
        );
    }
}
