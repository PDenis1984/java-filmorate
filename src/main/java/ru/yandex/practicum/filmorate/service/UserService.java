package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.model.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.exception.ValidationException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class UserService {

    private Map<Long, User> userMap;
    private long sequence = 0;


    public UserService() {

        this.userMap = new HashMap<>();
    }

    public User getUser(long id) {

        log.info("Поиск пользователя с id={}", id);

        User user = userMap.get(id);
        if (user == null) {
            log.error("Пользователь с id = {} не найден", id);;
            throw new UserNotFoundException("Пользователь с id =  " + id + " не найден");
        }
        return user;

    }

    public List<User> getUsers() {

        log.info("Получение всех пользователей");
        return userMap.values().stream().toList();
    }

    public User createUser(User user) throws ValidationException {

        log.info("Создание пользователя");
        log.trace("Создание пользователя: {}", user.toString());
        try {
            checkUserValid(user);
            long userID = ++sequence;
            User createdUser =  User.builder()
                    .login(user.getLogin())
                    .email(user.getEmail())
                    .name(user.getName())
                    .id(userID).dateOfBirth(user.getDateOfBirth()).build();

            userMap.put(userID, createdUser);
            return createdUser;
        } catch (ValidationException validationException) {
            log.error(validationException.getMessage());
            throw new ValidationException(validationException.getMessage());
        }
    }

    public User updateUser(User user, long id) {

        log.info("Обновление пользователя c id = {}", id);
        log.trace("Обновление пользователя: {}", user.toString());

        if (userMap.get(id) == null) {
            log.info("Пользователь с id = {} не найден. Создаем пользователя", id);
            return this.createUser(user);
        }
        try {
            checkUserValid(user);
            userMap.put(id, user);
        } catch (ValidationException validationException) {
            log.error(validationException.getMessage());
        }
        return user;
    }

    private boolean checkUserValid(User user) throws ValidationException {
        //Здесь все проверки должны сработать на контроллере за счет аннотации @valid, кроме пробела в логине
        if (user.getLogin().contains(" ")) {
            throw new ValidationException("Логин не должен содержать пробелы");
        }
        return true;
    }
}
