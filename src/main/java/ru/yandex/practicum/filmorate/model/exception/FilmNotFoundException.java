package ru.yandex.practicum.filmorate.model.exception;

public class FilmNotFoundException extends RuntimeException {

    public FilmNotFoundException(String message) {
        super(message);
    }
}
