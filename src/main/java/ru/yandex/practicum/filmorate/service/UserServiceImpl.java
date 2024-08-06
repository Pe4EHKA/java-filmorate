package ru.yandex.practicum.filmorate.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.Collection;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserStorage userStorage;

    @Override
    public User createUser(User user) {
        log.info("Creating user: {}", user);
        return userStorage.createUser(user);
    }

    @Override
    public User updateUser(User user) {
        log.info("Updating user: {}", user);
        return userStorage.updateUser(user);
    }

    @Override
    public User getUserById(long id) {
        return userStorage.getUserById(id)
                .orElseThrow(() -> new NotFoundException("User with id: %d not found".formatted(id)));
    }

    @Override
    public User addToFriends(long userId, long friendId) {
        User user = userStorage.getUserById(userId)
                .orElseThrow(() -> new NotFoundException("User with id: %d not found".formatted(userId)));
        User friend = userStorage.getUserById(friendId).orElseThrow(() -> new NotFoundException("Friend with id: %d not found".formatted(friendId)));
        log.debug("Adding user {} to friend {}", user, friend);
        userStorage.addToFriends(user, friend);
        log.debug("Added user {} to friend {}", user, friend);
        return friend;
    }

    @Override
    public User removeFromFriends(long userId, long friendId) {
        User user = userStorage.getUserById(userId)
                .orElseThrow(() -> new NotFoundException("User with id: %d not found".formatted(userId)));
        User friend = userStorage.getUserById(friendId)
                .orElseThrow(() -> new NotFoundException("Friend with id: %d not found".formatted(friendId)));
        log.debug("Removing user {} from friend {}", user, friend);
        userStorage.removeFromFriends(user, friend);
        log.debug("Removed user {} from friend {}", user, friend);
        return friend;
    }

    @Override
    public Collection<User> getAllUsers() {
        return userStorage.getAllUsers();
    }

    @Override
    public Collection<User> getMutualFriends(long userId, long friendId) {
        User user = userStorage.getUserById(userId).orElseThrow(() -> new NotFoundException("User with id: %d not found".formatted(userId)));
        User friend = userStorage.getUserById(friendId).orElseThrow(() -> new NotFoundException("Friend with id: %d not found".formatted(friendId)));
        log.info("Getting mutual friends of user {} from friend {}", user, friend);
        return userStorage.getMutualFriends(user, friend);
    }

    @Override
    public Collection<User> getFriendsList(long userId) {
        User user = userStorage.getUserById(userId).orElseThrow(() -> new NotFoundException("User with id: %d not found".formatted(userId)));
        return userStorage.getFriendsList(user);
    }
}
