package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;

@Data
@Builder
@EqualsAndHashCode(of = {"id", "login"})
@AllArgsConstructor
public class User {

    private final Long id;

    @NotBlank(message = "Электронная почта не может быть пустой и должна содержать символ @")
    @Email
    private String email;

    @NotBlank(message = "Логин не может быть пустым и содержать пробелы;")
    private final String login;

    private String name;
    // @NonNull
    @Past(message = "Дата рождения должна быть в прошлом")
    private LocalDate dateOfBirth;

}
