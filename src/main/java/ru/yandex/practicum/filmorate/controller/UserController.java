package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.intf.CrudInterface;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

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
    public User read(@Valid  @PathVariable Long id) {

        return userService.getUser(id);
    }

    @Override
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public User  create(@Valid  @RequestBody User user) {
        log.info(user.toString());
        return userService.createUser(user);
    }

    @Override
    @PutMapping("/{id}")
    public User update(@Valid @RequestBody User user) {

        return userService.updateUser(user);
    }

    @Override
    @GetMapping
    public List<User> getAll() {

        return userService.getUsers();
    }
}
