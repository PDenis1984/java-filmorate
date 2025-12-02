package ru.yandex.practicum.filmorate.storage;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.intf.InMemoryFilmInterface;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.model.exception.LikeNotFoundException;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Data
@RequiredArgsConstructor
@Slf4j
public class InMemoryFilmStorage implements InMemoryFilmInterface {

    private final Map<Long, Film> filmMap;
    private long sequence = 0;

    @Override
    public Film createFilm(Film  film) {

        long filmID = ++sequence;
        this.filmMap.put(filmID, film);
        return  film;
    }

    @Override
    public Film updatedFilm(Film film) {

        filmMap.put(film.getId(), film);
        return film;
    }

    @Override
    public boolean addLike(long filmId, long userId) {

        Film film = filmMap.get(filmId);
        if(film == null) {
            throw new FilmNotFoundException("Фильм с id  = "  + filmId + " не найден");
        }
        film.setLike(userId);
        filmMap.put(filmId, film);
        return true;
    }
    public boolean deleteLike(long filmId, long userId) {

        Film film = filmMap.get(filmId);
        if (film == null) {
            throw new FilmNotFoundException("Фильм с id  = "  + filmId + " не найден");
        }
        boolean isDeleted = film.deleteLike(userId);
        if (!isDeleted) {
            throw new LikeNotFoundException("Like от пользовыателя с id = "  + userId + " не найжен");
        }
        return isDeleted;
    }
    public List<Film> getTop10Films() {

        return filmMap.values().stream()
                .sorted(Comparator.comparingInt(f -> -f.getLikeSet().size()))
                .limit(10)
                .collect(Collectors.toList());

    }
    public Film getFilm(long filmId) {

        if (!filmMap.containsKey(filmId)) {
            log.error("Фильм с id = {} не найден. Создаем Фильм", filmId);
            throw new FilmNotFoundException("Фильм с id = " + filmId + " не найден");
        }
        return filmMap.get(filmId);
    }
    public List<Film> getAllFilms() {

        return filmMap.values().stream().toList();
    }
}
