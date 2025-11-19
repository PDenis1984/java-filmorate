package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.Duration;
import java.time.LocalDate;

/**
 * Film.
 */
@Data
@Builder
@EqualsAndHashCode(of = {"id","name"})
@AllArgsConstructor
public class Film {

    private final Long id;

    private final String name;

    private String description;

    private LocalDate releaseDate;

    private Duration duration;

    public void setDuration(int minutes) {

        this.duration = Duration.ofMinutes(minutes);
    }

    public long getDuration() {

        return  this.duration.toMinutes();
    }
}
