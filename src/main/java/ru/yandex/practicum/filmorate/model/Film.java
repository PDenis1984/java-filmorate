package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;
import java.util.Set;

/**
 * Film.
 */
@Data
@Builder
@EqualsAndHashCode(of = {"id", "name"})
@AllArgsConstructor
public class Film {

    private final Long id;
    private final Set<Long> likeSet;

    private String name;

    private String description;

    private LocalDate releaseDate;

    private long duration;

    public void setLike(long filmId) {

        this.likeSet.add(filmId);
    }
}