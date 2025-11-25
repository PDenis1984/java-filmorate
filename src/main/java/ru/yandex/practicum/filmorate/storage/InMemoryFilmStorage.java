package ru.yandex.practicum.filmorate.storage;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import ru.yandex.practicum.filmorate.intf.InMemoryFilmInterface;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Map;

@Data
@RequiredArgsConstructor
public class InMemoryFilmStorage implements InMemoryFilmInterface {

    private final Map<Long, Film> filmMap;
    private long sequence = 0;

    @Override
    Film createFilm(Film  film) {

        long filmID = ++sequence;
        Film createdFilm = buildFilm(film, filmID);
        this.filmMap.put(filmID, film);
        return  createdFilm;
    }


    private Film buildFilm(Film film, long filmID) {

        return Film.builder()
                .id(filmID)
                .name(film.getName())
                .description(film.getDescription())
                .releaseDate(film.getReleaseDate())
                .duration(film.getDuration()).build();

    }
}
