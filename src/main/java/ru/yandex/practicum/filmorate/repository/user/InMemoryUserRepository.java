package ru.yandex.practicum.filmorate.repository.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;

@Component
@Slf4j
public class InMemoryUserRepository implements UserRepository {

    private final Map<Long, User> users = new HashMap<>();
    private final Map<Long, Set<Long>> userFriendIds = new HashMap<>();
    private long seq = 0;

    @Override
    public Collection<User> getAllUsers() {
        log.info("Get all users");
        return users.values();
    }

    @Override
    public Optional<User> getUserById(long id) {
        log.info("Get user by id: {}", id);
        return Optional.ofNullable(users.get(id));
    }

    @Override
    public User createUser(User user) {
        log.debug("Creating User: {}", user);
        user.setId(generateNextId());
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        users.put(user.getId(), user);
        log.debug("User created: {}", user);
        return user;
    }

    @Override
    public User updateUser(User user) {
        User oldUser = getUserById(user.getId())
                .orElseThrow(() -> new NotFoundException("User with id: %s not found".formatted(user.getId())));
        log.debug("Updating User: {}", oldUser);
        if (user.getName() != null) oldUser.setName(user.getName());
        if (user.getLogin() != null) oldUser.setLogin(user.getLogin());
        if (user.getEmail() != null) oldUser.setEmail(user.getEmail());
        if (user.getBirthday() != null) oldUser.setBirthday(user.getBirthday());
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        log.debug("User updated: {}", oldUser);
        return oldUser;
    }

    @Override
    public boolean containsUser(long userId) {
        return false;
    }

    private Long generateNextId() {
        return ++seq;
    }
}
