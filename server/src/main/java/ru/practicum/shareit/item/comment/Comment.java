package ru.practicum.shareit.item.comment;

import lombok.NoArgsConstructor;
import lombok.Data;
import jakarta.persistence.*;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.item.Item;


import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "comments")
@NoArgsConstructor
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String text;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id")
    private User author;

    @Column(name = "created_date")
    private LocalDateTime created;

    public Comment(String text, Item item, User author, LocalDateTime now) {
        this.item = item;
        this.author = author;
        this.created = now;
        this.text = text;
    }
}
