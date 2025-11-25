package ru.yandex.practicum.filmorate.service;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.intf.InMemoryFilmInterface;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.model.exception.ValidationException;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@Data
public class FilmService {

    private static final LocalDate DATE_OF_BORN_CINEMA = LocalDate.of(1895, 12, 25);

    @Autowired
    private InMemoryFilmInterface inMemoryFilmStorage;
    public FilmService(InMemoryFilmInterface cInMemoryFilmInterface) {

        this.inMemoryFilmStorage = cInMemoryFilmInterface;
    }

    public Film createFilm(Film film) throws ValidationException {

        log.info("Добавление фильма");
        log.debug("Добавление фильма: {}", film.toString());
        checkFilmValid(film);
        Film createdFilm = inMemoryFilmStorage.createFilm(film);
        return createdFilm;
    }

    public Film updateFilm(Film film, long id) throws FilmNotFoundException, ValidationException {

        log.info("Обновление фильма c id = {}", id);
        log.trace("Обновление фильма: {}", film.toString());

        if (filmMap.get(id) == null) {
            log.error("Фильм с id = {} не найден. Создаем Фильм", id);
            throw new FilmNotFoundException("Фильм с id = " + id + " не найден");
        }
        checkFilmValid(film);
        Film updatedFilm = buildFilm(film, film.getId());
        updatedFilm.setDuration(film.getDuration());
        filmMap.put(id, updatedFilm);
        return updatedFilm;
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

    private void checkFilmValid(Film film) {
        // здесь проверим все ограничения вручную
        if (film.getName() == null || film.getName().isBlank()) {

            throw new ValidationException("Название фильма не может быть пустым");
        }

        if (film.getDescription() != null && film.getDescription().length() > 200) {

            throw new ValidationException("Длина описания должна быть менее 200 символов");
        }

        if (film.getReleaseDate() == null) {

            throw new ValidationException("Дата релиза фильма не должна быть пустой");
        }

        if (film.getReleaseDate().isBefore(DATE_OF_BORN_CINEMA)) {

            throw new ValidationException("Дата релиза фильма не ранее 28 декабря 1895 года");
        }

        if (film.getDuration() <= 0) {

            throw new ValidationException("Длительность фильма должна быть положительным числом");
        }
    }

}
