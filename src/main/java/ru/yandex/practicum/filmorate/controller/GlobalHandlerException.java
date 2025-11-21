package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.yandex.practicum.filmorate.model.ErrorResponse;
import ru.yandex.practicum.filmorate.model.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.model.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.exception.ValidationException;

@Slf4j
@RestControllerAdvice
public class GlobalHandlerException {


    @ExceptionHandler(ValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleValidationException(ValidationException vEx) {

        log.error("Ошибка валидации: {}", vEx.getMessage());
        return new ErrorResponse(vEx.getMessage(), 400);
    }

    @ExceptionHandler(FilmNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleUserFilmNotFoundException(FilmNotFoundException uEx) {
        log.warn("Фильм не найден: {}", uEx.getMessage());
        return new ErrorResponse(uEx.getMessage(), 404);
    }

    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleUserNotFoundException(UserNotFoundException uEx) {
        log.warn("Пользователь не найден: {}", uEx.getMessage());
        return new ErrorResponse(uEx.getMessage(), 404);
    }
}