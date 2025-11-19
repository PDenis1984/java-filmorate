package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.model.exception.ValidationException;

import java.time.Duration;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service

public class FilmService {

    private Map<Long, Film> filmMap;
    private long sequence = 0;

    public FilmService() {

        this.filmMap = new HashMap<>();
    }

    public Film createFilm(Film film) throws ValidationException {

        log.info("Добавление фильма");
        log.debug("Добавление фильма: {}", film.toString());
        try {
            checkFilmValid(film);
            long filmID = ++sequence;
            Film createdFilm = Film.builder()
                    .id(filmID)
                    .name(film.getName())
                    .description(film.getDescription())
                    .releaseDate(film.getReleaseDate())
                    .build();
            createdFilm.setDuration(film.getDuration());
            filmMap.put(sequence++, createdFilm);
            return createdFilm;
        } catch (ValidationException validationException) {
            log.error(validationException.getMessage());
            throw validationException;
        }
    }

    public Film updateFilm(Film film, long id) throws FilmNotFoundException, ValidationException {

        log.info("Обновление фильма c id = {}", id);
        log.trace("Обновление фильма: {}", film.toString());

        if (filmMap.get(id) == null) {
            log.error("Фильм с id = {} не найден. Создаем Фильм", id);
            throw new FilmNotFoundException("Фильм с id = " + id + " не найден");
        }
        try {
            checkFilmValid(film);
            Film updatedFilm = Film.builder()
                    .id(id)
                    .name(film.getName())
                    .description(film.getDescription())
                    .releaseDate(film.getReleaseDate())
                    .build();
            updatedFilm.setDuration(film.getDuration());
            filmMap.put(id, updatedFilm);
            return updatedFilm;
        } catch (ValidationException validationException) {
            log.error(validationException.getMessage());
            throw validationException;
        }
    }

    public Film getFilm(Long id) {

        if (filmMap.containsKey(id)) {

            return filmMap.get(id);
        }
        throw new FilmNotFoundException("Фильм с id = " + id + " не найден");
    }

    public List<Film> getFilms() {

        return filmMap.values().stream().toList();
    }

    private boolean checkFilmValid(Film film) {
        // здесь проверим все ограничения вручную
        if (film.getName().isBlank()) {

            throw new ValidationException("Название фильма не может быть пустым");
        } else if (film.getDescription().length() > 200) {

            throw new ValidationException("Длина описания должна быть менее 200 символов");
        } else if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 25))) {

            throw new ValidationException("Дата релиза фильма не ранее 28 декабря 1895 года");
        } else if (film.getDuration() < 0) {

            throw new ValidationException("Длительность фильма должна быть положительным числом");
        }

        return true;
    }
}
