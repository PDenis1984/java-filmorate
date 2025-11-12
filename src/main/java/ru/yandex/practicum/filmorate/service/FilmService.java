package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.exception.ValidationException;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service

public class FilmService {

    private Map<Long, Film> filmMap;
    private long sequence = 0;

    public  FilmService() {

        this.filmMap = new HashMap<>();
    }
    public Film createFilm(Film film) {

        filmMap.put(sequence++, film);
        return film;
    }

    public Film getFilm(Long id) {

        if (filmMap.containsKey(id)) {

            return filmMap.get(id);
        }
        return null;
    }

    public List<Film> getFilms() {

        return filmMap.values().stream().toList();
    }
    private boolean checkFilmValid(Film film) {
        // здесь проверим все ограничения вручную
        if(film.getName().isBlank()) {

            throw  new ValidationException("Название фильма не может быть пустым");
        } else if(film.getDescription().length() > 200) {

            throw new ValidationException("Длина описания должна быть менее 200 символов");
        } else if(film.getReleaseDate().isAfter(LocalDate.of(1895,12,25))) {

            throw new ValidationException("Дата ерлиза фильма не ранее 28 декабря 1895 года");
        } else if (film.getDuration().toMinutes() < 0) {

            throw new ValidationException("Длительность фильма должна быть положительным числом");
        }

        return true;
    }
}
