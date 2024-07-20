package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.annotation.Marker;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {

    private final Map<Long, User> users = new HashMap<>();
    private int seq = 0;

    @GetMapping
    public Collection<User> getAllUsers() {
        return users.values();
    }

    @PostMapping
    @Validated({Marker.OnCreate.class})
    public User createUser(@Valid @RequestBody User user) {
        checkNameEmpty(user);

        log.debug("Creating User: {}", user);
        user.setId(generateNextId());
        users.put(user.getId(), user);
        log.debug("User created: {}", user);
        return user;
    }

    @PutMapping
    @Validated({Marker.OnUpdate.class})
    public User updateUser(@Valid @RequestBody User user) {
        checkNameEmpty(user);

        User oldUser = users.get(user.getId());
        log.debug("Updating User: {}", oldUser);
        oldUser.setName(user.getName());
        oldUser.setLogin(user.getLogin());
        oldUser.setEmail(user.getEmail());
        oldUser.setBirthday(user.getBirthday());
        log.debug("User updated: {}", oldUser);
        return oldUser;
    }

    private void checkNameEmpty(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
    }


    private Long generateNextId() {
        return (long) ++seq;
    }
}
