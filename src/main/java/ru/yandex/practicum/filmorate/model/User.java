package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

import java.time.LocalDate;

@Data
@Builder
public class User {

    @NonNull
    private final Long id;

    @NonNull
    private String enail;

    @NonNull
    private final String login;
    private String name;
    private LocalDate dateOfBirth;

}
