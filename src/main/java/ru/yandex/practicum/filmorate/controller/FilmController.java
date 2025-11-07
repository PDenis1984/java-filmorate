package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.intf.CrudInterface;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/films")
public class FilmController implements CrudInterface<Film> {

    private final static Logger log = LoggerFactory.getLogger(FilmController.class);

    private final FilmService filmService;

    public FilmController(FilmService cFilmService) {

        this.filmService = cFilmService;
    }

    @PostMapping
    @Override
    public Film create(@RequestBody Film film) {

        return filmService.createFilm(film);
    }

    @PutMapping()
    @Override
        public Film update(@RequestBody Film film) {

        return film;
    }

    @Override
    @GetMapping
    public List<Film> getAll() {

        return filmMap.values().stream().toList();
    }
    @Override
    @GetMapping("/films/{id}")
    public Film read(@PathVariable Long id) {

        return filmService.getFilm(id);
    }
}
