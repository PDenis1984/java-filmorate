package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.intf.CrudInterface;
import ru.yandex.practicum.filmorate.model.ErrorResponse;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.model.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.exception.UserNotFoundException;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/users")
public class UserController implements CrudInterface<User> {

    private final UserService userService;

    public UserController(UserService cUserService) {

        this.userService = cUserService;
    }

    @Override
    @GetMapping("/{id}")
    public User read(@Valid @PathVariable Long id) {

        log.info("Получение пользователя с id = {}",id);
        return userService.getUser(id);
    }

    @Override
    @PostMapping
    public ResponseEntity<User> create(@Valid  @RequestBody User user) {

        log.info("Создание пользователя");
        log.debug("Создание пользователя:{}", user.toString());
        User createdUser = userService.createUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }

    @Override
    @PutMapping("/{id}")
    public User update(@PathVariable long id, @Valid @RequestBody User user) {

        log.info("Обновление пользователя с id = {}", id);
        log.debug("Обновление пользователя: {}", user.toString());
        return userService.updateUser(user,id);
    }

    @Override
    @GetMapping
    public List<User> getAll() {

        log.info("Получение списка пользователей");
        return userService.getUsers();
    }

    //Exception Handlers
    @ExceptionHandler(ValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleValidationException(ValidationException vEx) {
        log.error("Ошибка валидации: {}", vEx.getMessage());
        return new ErrorResponse(vEx.getMessage(), 400);
    }

    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleUserNotFoundException(UserNotFoundException uEx) {
        log.warn("Пользователь не найден: {}", uEx.getMessage());
        return new ErrorResponse(uEx.getMessage(), 404);
    }
}
