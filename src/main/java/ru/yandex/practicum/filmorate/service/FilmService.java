package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;

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

        if()

        return true;
    }
}
