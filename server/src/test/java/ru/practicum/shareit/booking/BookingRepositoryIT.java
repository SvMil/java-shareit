package ru.practicum.shareit.booking;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.service.StateStatus;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
class BookingRepositoryIT {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ItemRepository itemRepository;

    private final User userForTests = new User(null, "test", "test@gmail.com");

    private final Item itemForTests = new Item(null, "test", "test", true, null);

    private final User userForTests2 = new User(null, "test2", "test2@gmail.com");

    private final Item itemForTests2 = new Item(null, "test2", "test2", true, null);

    private final Booking bookingForTests = new Booking(null, LocalDateTime.now().plusHours(24), LocalDateTime.now(), itemForTests, userForTests, BookingStatus.WAITING);

    private final Booking bookingForTests2 = new Booking(null, LocalDateTime.now(), LocalDateTime.now().minusDays(2), itemForTests2, userForTests2, BookingStatus.APPROVED);

    private final Booking bookingForTests3 = new Booking(null, LocalDateTime.now().minusDays(2), LocalDateTime.now().plusDays(2), itemForTests2, userForTests2, BookingStatus.APPROVED);

    @BeforeEach
    void setUp() {
        userRepository.save(userForTests);
        userRepository.save(userForTests2);
        itemRepository.save(itemForTests);
        itemRepository.save(itemForTests2);
        bookingRepository.save(bookingForTests);
        bookingRepository.save(bookingForTests2);
        bookingRepository.save(bookingForTests3);

    }

    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
        itemRepository.deleteAll();
        bookingRepository.deleteAll();
    }

    @Test
    void findAllByOwnerAndStatusOrderByStartDesc() {
        List<Booking> all = bookingRepository.findByBookerIdOrderByStartDesc(userForTests.getId());
        assertEquals(1, all.size());
        assertEquals(userForTests, all.get(0).getBooker());
    }

    @Test
    void findAllByItemInAndStatusOrderByStartAsc() {
        List<Booking> all = bookingRepository.findAllByItemInAndStatusOrderByStartAsc(List.of(itemForTests), BookingStatus.WAITING);
        assertEquals(1, all.size());
        assertEquals(userForTests, all.get(0).getBooker());
    }

}