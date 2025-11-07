package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Map;

@Service
public class FilmService {

    private Map<Long, Film> filmMap;
    private long sequence;

    public Film createFilm(Film film) {

        filmMap.put(film.getId(), film);
        return film;
    }

    public Film getFilm(Long id) {

        if (filmMap.containsKey(id)) {

            return filmMap.get(id);
        }
        return null;
    }
}
