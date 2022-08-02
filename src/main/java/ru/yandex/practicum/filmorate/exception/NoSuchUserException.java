package ru.yandex.practicum.filmorate.exception;

public class NoSuchUserException extends Exception {
    public NoSuchUserException(String message) {
        super(message);
    }
}
