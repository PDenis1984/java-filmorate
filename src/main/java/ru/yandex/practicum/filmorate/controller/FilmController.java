package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.intf.CrudInterface;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController implements CrudInterface<Film> {

    private final FilmService filmService;

    public FilmController(FilmService cFilmService) {

        this.filmService = cFilmService;
    }

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<Film> read(@PathVariable Long id) {

        log.info("Получение фильма с id = {}", id);
        return ResponseEntity.status(HttpStatus.OK).body(filmService.getFilm(id));
    }


    @PostMapping
    @Override
    public ResponseEntity<Film> create(@RequestBody Film film) {

        log.info("Добавление фильма");
        log.debug("Добавление фильма:{}", film.toString());

        Film createdFilm = filmService.createFilm(film);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdFilm);
    }


    @Override
    @PutMapping("/{id}")
    public ResponseEntity<Film> update(@PathVariable long id, @RequestBody Film film) {

        log.info("Обновление фильма с id = {}", id);
        log.debug("Обновление фильма : {}", film.toString());
        Film updatedFilm = filmService.updateFilm(film, id);
        return ResponseEntity.status(HttpStatus.OK).body(updatedFilm);
    }

    @Override
    @PutMapping
    public ResponseEntity<Film> update(@RequestBody Film film) {

        log.info("Обновление фильма с id = {}", film.getId());
        log.debug("Обновление фильма : {}", film.toString());

        Film updatedFilm = filmService.updateFilm(film, film.getId());
        return ResponseEntity.status(HttpStatus.OK).body(updatedFilm);

    }


    @Override
    @GetMapping
    public ResponseEntity<List<Film>> getAll() {

        log.info("Получение списка фильмов");
        return ResponseEntity.status(HttpStatus.OK).body(filmService.getFilms());
    }
}
