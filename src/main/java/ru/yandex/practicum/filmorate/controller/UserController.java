package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.annotation.Marker;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.user.UserService;

import java.util.Collection;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public Collection<User> getAllUsers() {
        log.info("Request for all users");
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    public User getUserById(@PathVariable(name = "id") long userId) {
        log.info("Request for user with id {}", userId);
        return userService.getUserById(userId);
    }

    @GetMapping("/{id}/friends")
    public Collection<User> getUserFriends(@PathVariable(name = "id") long userId) {
        log.info("Request for friends of user with id {}", userId);
        return userService.getFriendsList(userId);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public Collection<User> getUserFriendsCommon(@PathVariable(name = "id") long userId,
                                                 @PathVariable(name = "otherId") long otherId) {
        log.info("Request for common friends of user with id {} and other id {}", userId, otherId);
        return userService.getMutualFriends(userId, otherId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Validated({Marker.OnCreate.class})
    public User createUser(@Valid @RequestBody User user) {
        log.info("Request to create film: {}", user);
        if (user == null) {
            throw new ValidationException("User is null");
        }
        return userService.createUser(user);
    }

    @PutMapping
    @Validated({Marker.OnUpdate.class})
    public User updateUser(@Valid @RequestBody User user) {
        log.info("Request to update user: {}", user);
        if (user == null) {
            throw new ValidationException("User is null");
        }
        return userService.updateUser(user);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public User addToFriends(@PathVariable(name = "id") long userId,
                             @PathVariable(name = "friendId") long friendId) {
        return userService.addToFriends(userId, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public User removeFromFriends(@PathVariable(name = "id") long userId,
                                  @PathVariable(name = "friendId") long friendId) {
        return userService.removeFromFriends(userId, friendId);
    }
}
