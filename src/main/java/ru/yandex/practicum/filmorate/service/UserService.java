package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.model.exception.ValidationException;

import java.time.LocalDate;
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
            userMap.put(sequence++, user);
        }  catch (ValidationException validationException) {
            log.error(validationException.getMessage());
        }
        return user;
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

    private void checkUserValid(User user) throws ValidationException {

        if (!user.getEnail().contains("@")) {
            throw new ValidationException("Неверный формат email");
        } else if (user.getLogin().isBlank()) {
            throw new ValidationException("Имя пользователя не должно пустым или содержать пробелы");
        } else if (user.getDateOfBirth().isAfter(LocalDate.now())) {
            throw new ValidationException("Дата рождения не должна быть меньше текущей даты");
        }
    }
}
