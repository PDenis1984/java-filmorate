package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.Duration;
import java.time.LocalDate;

/**
 * Film.
 */
@Data
@Builder
@EqualsAndHashCode(of = {"id","name"})
public class Film {

    private final Long id;

    @NotBlank
    private final String name;

    @Size(min=1, max=200)
    private String description;


    private LocalDate releaseDate;
    private Duration duration;

}
