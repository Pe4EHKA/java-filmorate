package ru.yandex.practicum.filmorate.service.user;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.repository.user.UserRepository;

import java.util.Collection;
import java.util.List;

@Service
@Slf4j
public class UserServiceImpl implements UserService {


    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(@Qualifier("inMemoryUserRepository") UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User createUser(User user) {
        log.info("Creating user: {}", user);
        return userRepository.createUser(user);
    }

    @Override
    public User updateUser(User user) {
        log.info("Updating user: {}", user);
        return userRepository.updateUser(user);
    }

    @Override
    public User getUserById(long id) {
        return userRepository.getUserById(id)
                .orElseThrow(() -> new NotFoundException("User with id: %d not found".formatted(id)));
    }

    @Override
    public User addToFriends(long userId, long friendId) {
        User user = userRepository.getUserById(userId)
                .orElseThrow(() -> new NotFoundException("User with id: %d not found".formatted(userId)));
        User friend = userRepository.getUserById(friendId).orElseThrow(() -> new NotFoundException("Friend with id: %d not found".formatted(friendId)));
        log.debug("Adding user {} to friend {}", user, friend);
//        userRepository.addToFriends(user, friend);
        log.debug("Added user {} to friend {}", user, friend);
        return friend;
    }

    @Override
    public User removeFromFriends(long userId, long friendId) {
        User user = userRepository.getUserById(userId)
                .orElseThrow(() -> new NotFoundException("User with id: %d not found".formatted(userId)));
        User friend = userRepository.getUserById(friendId)
                .orElseThrow(() -> new NotFoundException("Friend with id: %d not found".formatted(friendId)));
        log.debug("Removing user {} from friend {}", user, friend);
//        userRepository.removeFromFriends(user, friend);
        log.debug("Removed user {} from friend {}", user, friend);
        return friend;
    }

    @Override
    public Collection<User> getAllUsers() {
        return userRepository.getAllUsers();
    }

    @Override
    public Collection<User> getMutualFriends(long userId, long friendId) {
        User user = userRepository.getUserById(userId).orElseThrow(() -> new NotFoundException("User with id: %d not found".formatted(userId)));
        User friend = userRepository.getUserById(friendId).orElseThrow(() -> new NotFoundException("Friend with id: %d not found".formatted(friendId)));
        log.info("Getting mutual friends of user {} from friend {}", user, friend);
//        return userRepository.getMutualFriends(user, friend);
        return List.of();
    }

    @Override
    public Collection<User> getFriendsList(long userId) {
        User user = userRepository.getUserById(userId).orElseThrow(() -> new NotFoundException("User with id: %d not found".formatted(userId)));
//        return userRepository.getFriendsList(user);
        return List.of();
    }
}
