package ru.practicum.shareit.booking.model;

import jakarta.persistence.*;
import lombok.NoArgsConstructor;
import lombok.Data;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.item.Item;

import java.time.LocalDateTime;


@Data
@Entity
@Table(name = "bookings")
@NoArgsConstructor
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "booker_id")
    private User booker;

    @Enumerated(EnumType.STRING)
    @Column
    private BookingStatus status = BookingStatus.WAITING;

    @Column(name = "start_date")
    private LocalDateTime start;

    @Column(name = "end_date")
    private LocalDateTime end;
}
