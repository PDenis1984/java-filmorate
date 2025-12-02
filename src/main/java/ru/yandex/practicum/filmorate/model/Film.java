package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;
import java.util.Set;
import java.util.TreeSet;

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

    public void setLike(long userId) {

        this.likeSet.add(userId);
    }

    public boolean deleteLike(long userId) {


        if (this.likeSet.contains(userId)) {
            this.likeSet.remove(userId);
            return true;
        }
        return false;
    }
    public Set<Long> getLikes() {

        return new TreeSet<Film>(likeSet);
    }
}