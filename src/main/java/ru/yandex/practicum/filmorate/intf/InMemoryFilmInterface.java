package ru.yandex.practicum.filmorate.intf;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface InMemoryFilmInterface {


    Film createFilm(Film film);
    Film updatedFilm(Film film);
    boolean addLike(long filmId, long userId);
    boolean deleteLike(long filmId, long userId);
    List<Film> getTop10Films();
    Film getFilm(long filmId);
    List<Film> getAllFilms();
}
