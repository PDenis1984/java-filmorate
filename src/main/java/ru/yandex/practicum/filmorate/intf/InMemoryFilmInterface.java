package ru.yandex.practicum.filmorate.intf;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface InMemoryFilmInterface {

    Film addLike(Film film);
    Film creeateFilm(Film film);
    Film updatedFilm(Film film);
    boolean addLike(long filmId);
    boolean deleteLike(long filmId);
    List<Film> getTop10Films();
    Film getFilm(long filmId);
    List<Film> getAllFilms();
}
