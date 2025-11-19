package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;

/**
 * Film.
 */
@Data
@Builder
@EqualsAndHashCode(of = {"id", "name"})
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Film {

    private final Long id;

    private String name;

    private String description;

    private LocalDate releaseDate;

    private long duration;
}