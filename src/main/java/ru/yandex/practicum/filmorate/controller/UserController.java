package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.annotation.Marker;
import ru.yandex.practicum.filmorate.exception.ValidationException;
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
        log.info("Request to create film: {}", user);
        if (user == null) {
            throw new ValidationException("User is null");
        }

        log.debug("Creating User: {}", user);
        user.setId(generateNextId());
        user.checkNameEmpty();
        users.put(user.getId(), user);
        log.debug("User created: {}", user);
        return user;
    }

    @PutMapping
    @Validated({Marker.OnUpdate.class})
    public User updateUser(@Valid @RequestBody User user) {
        log.info("Request to update film: {}", user);
        if (user == null) {
            throw new ValidationException("User is null");
        }

        User oldUser = users.get(user.getId());
        if (oldUser == null) throw new ValidationException("User not found");
        log.debug("Updating User: {}", oldUser);
        if (user.getName() != null) oldUser.setName(user.getName());
        if (user.getLogin() != null) oldUser.setLogin(user.getLogin());
        if (user.getEmail() != null) oldUser.setEmail(user.getEmail());
        if (user.getBirthday() != null) oldUser.setBirthday(user.getBirthday());
        user.checkNameEmpty();
        log.debug("User updated: {}", oldUser);
        return oldUser;
    }


    private Long generateNextId() {
        return (long) ++seq;
    }
}
