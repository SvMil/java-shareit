package ru.practicum.shareit.exception;

public class ItemAlreadyBooking extends RuntimeException {
    public ItemAlreadyBooking(String message) {
        super(message);
    }
}
