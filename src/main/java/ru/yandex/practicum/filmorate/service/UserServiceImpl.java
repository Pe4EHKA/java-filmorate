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
        return userStorage.getUserById(id);
    }

    @Override
    public User addToFriends(long userId, long friendId) {
        User user = userStorage.getUserById(userId);
        User friend = userStorage.getUserById(friendId);
        if (userStorage.getUserById(user.getId()) == null) {
            throw new NotFoundException("User with id " + user.getId() + " not found");
        }
        if (userStorage.getUserById(friend.getId()) == null) {
            throw new NotFoundException("Friend with id " + friend.getId() + " not found");
        }
        log.debug("Adding user {} to friend {}", user, friend);
        userStorage.addToFriends(user, friend);
        log.debug("Added user {} to friend {}", user, friend);
        return userStorage.getUserById(friend.getId());
    }

    @Override
    public User removeFromFriends(long userId, long friendId) {
        User user = userStorage.getUserById(userId);
        User friend = userStorage.getUserById(friendId);
        if (userStorage.getUserById(user.getId()) == null) {
            throw new NotFoundException("User with id " + user.getId() + " not found");
        }
        if (userStorage.getUserById(friend.getId()) == null) {
            throw new NotFoundException("Friend with id " + friend.getId() + " not found");
        }
        log.debug("Removing user {} from friend {}", user, friend);
        userStorage.removeFromFriends(user, friend);
        log.debug("Removed user {} from friend {}", user, friend);
        return userStorage.getUserById(friend.getId());
    }

    @Override
    public Collection<User> getAllUsers() {
        return userStorage.getAllUsers();
    }

    @Override
    public Collection<User> getMutualFriends(long userId, long friendId) {
        User user = userStorage.getUserById(userId);
        User friend = userStorage.getUserById(friendId);
        if (userStorage.getUserById(user.getId()) == null) {
            throw new NotFoundException("User with id " + user.getId() + " not found");
        }
        if (userStorage.getUserById(friend.getId()) == null) {
            throw new NotFoundException("Friend with id " + friend.getId() + " not found");
        }
        log.info("Getting mutual friends of user {} from friend {}", user, friend);
        return userStorage.getMutualFriends(user, friend);
    }

    @Override
    public Collection<User> getFriendsList(long userId) {
        User user = userStorage.getUserById(userId);
        if (userStorage.getUserById(user.getId()) == null) {
            throw new NotFoundException("User with id " + user.getId() + " not found");
        }
        return userStorage.getFriendsList(user);
    }
}
