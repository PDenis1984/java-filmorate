package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
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
        return userMap.get(id);

    }

    public List<User> getUsers() {

        log.info("Получение всех пользователей");
        return userMap.values().stream().toList();
    }

    public User createUser(User user) {

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
            return user;
        }
    }

    public User updateUser(User user) {

        log.info("Обновление пользователя");
        log.trace("Обновление пользователя: {}", user.toString());
        try {
            checkUserValid(user);
            userMap.put(user.getId(), user);
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
