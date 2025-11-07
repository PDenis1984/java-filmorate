package ru.yandex.practicum.filmorate.model;

import lombok.*;

import java.time.Duration;
import java.time.LocalDate;

/**
 * Film.
 */
@Data
@Builder
public class Film {

    @NonNull
    private final Long id;
    @NonNull
    private final String name;
    private String description;
    private LocalDate releaseDate;
    private Duration duration;

}
