package ru.yandex.practicum.filmorate.model.exception;

public class LikeNotFoundException extends RuntimeException{

    public LikeNotFoundException(final String message) {
        super(message);
    }
}
