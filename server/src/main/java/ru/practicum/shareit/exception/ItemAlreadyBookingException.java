package ru.practicum.shareit.exception;

public class ItemAlreadyBookingException extends RuntimeException {
    public ItemAlreadyBookingException(String message) {
        super(message);
    }
}
