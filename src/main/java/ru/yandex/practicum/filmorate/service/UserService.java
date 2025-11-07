package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Map;

@Service
public class UserService {

    private Map<Long, User> userMap;
    private long sequence;


    public User getUser(long id) {

        return userMap.get(id);
    }

    public List<User> getUsers() {

        return userMap.values().stream().toList();
    }

    public User createUser(User user) {

        userMap.put(sequence++, user);
        return user;
    }
}
