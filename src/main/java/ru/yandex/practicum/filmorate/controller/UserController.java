package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.intf.CrudInterface;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController implements CrudInterface<User> {

    private final UserService userService;

    public UserController(UserService cUserService) {

        this.userService = cUserService;
    }

    @Override
    @GetMapping("/{id}")
    public User read(@Valid @PathVariable Long id) {

        log.info("Получение пользователя с id = {}", id);
        return userService.getUser(id);
    }

    @Override
    @PostMapping
        public ResponseEntity<User> create(@Valid @RequestBody User user) {

        log.info("Создание пользователя");
        log.debug("Создание пользователя:{}", user.toString());
        User createdUser = userService.createUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }

    @Override
    @PutMapping("/{id}")
    public ResponseEntity<User> update(@PathVariable long id, @Valid @RequestBody User user) {

        log.info("Обновление пользователя с id = {}", id);
        log.info("Обновление пользователя: {}", user.toString());
        User updatedUser = userService.updateUser(user, id);
        return ResponseEntity.status(HttpStatus.OK).body(updatedUser);
    }


    @Override
    @PutMapping(params = "!id")
    public ResponseEntity<User> update(@Valid @RequestBody User user) {

        log.info("Обновление пользователя с id = {}", user.getId());
        log.debug("Обновление пользователя: {}", user.toString());
        User updateUser = userService.updateUser(user, user.getId());
        return ResponseEntity.status(HttpStatus.OK).body(updateUser);
    }

    @Override
    @GetMapping
    public ResponseEntity<List<User>> getAll() {

        log.info("Получение списка пользователей");

        return ResponseEntity.status(HttpStatus.OK).body(userService.getUsers());
    }
}
