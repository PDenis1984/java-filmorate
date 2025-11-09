package ru.yandex.practicum.filmorate.model.exception;

public class ValidationException extends RuntimeException {

    public ValidationException(String message) {
        super(message);
    }
}

