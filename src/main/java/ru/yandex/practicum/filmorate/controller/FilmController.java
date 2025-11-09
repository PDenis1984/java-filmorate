package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.intf.CrudInterface;
import ru.yandex.practicum.filmorate.model.Film;
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

        return filmService.getFilms();
    }
    @Override
    @GetMapping("/films/{id}")
    public Film read(@PathVariable Long id) {

        return filmService.getFilm(id);
    }
}
