package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.intf.CrudInterface;
import ru.yandex.practicum.filmorate.model.ErrorResponse;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.model.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.exception.ValidationException;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/films")
public class FilmController implements CrudInterface<Film> {

    private final FilmService filmService;

    public FilmController(FilmService cFilmService) {

        this.filmService = cFilmService;
    }

    @Override
    @GetMapping("/films/{id}")
    public Film read(@PathVariable Long id) {

        log.info("Получение фильма с id = {}" , id);
        return filmService.getFilm(id);
    }


    @PostMapping
    @Override
    @ResponseStatus(HttpStatus.CREATED)
    public Film create(@RequestBody Film film) {

        log.info("Добавление фильма");
        log.debug("Добавление фильма:{}", film.toString());
        return filmService.createFilm(film);
    }


    @Override
    @PutMapping("/{id}")
    public Film update(@PathVariable long id, @RequestBody Film film) {

        log.info("Обновление фильма с id = {}", id);
        log.debug("Обновление фильма : {}", film.toString());
        return filmService.updateFilm(film, id);
    }

    @Override
    @GetMapping
    public List<Film> getAll() {

        log.info("Получение списка фильмов");
        return filmService.getFilms();
    }

    //Exception Handlers
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
}
