package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;
import java.util.stream.Collectors;

@Component
@Slf4j
public class InMemoryUserStorage implements UserStorage {

    private final Map<Long, User> users = new HashMap<>();
    private final Map<Long, Set<Long>> userFriendIds = new HashMap<>();
    private long seq = 0;

    @Override
    public Collection<User> getAllUsers() {
        log.info("Get all users");
        return users.values();
    }

    @Override
    public User saveUser(User user) {
        return users.put(user.getId(), user);
    }

    @Override
    public User getUserById(long id) {
        log.info("Get user by id: {}", id);
        User user = users.get(id);
        if (user == null) {
            throw new NotFoundException("User with id: %s not found".formatted(id));
        }
        log.info("User with id: {} found and returned", id);
        return user;
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
        User oldUser = users.get(user.getId());
        if (oldUser == null) throw new NotFoundException("User with id: %s not found".formatted(user.getId()));
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
    public void addToFriends(User user, User friend) {
        log.debug("Adding to friends: User = {}, friend = {}", user, friend);
        Set<Long> uFriendIds = userFriendIds.computeIfAbsent(user.getId(), id -> new HashSet<>());
        uFriendIds.add(friend.getId());

        Set<Long> fFriendIds = userFriendIds.computeIfAbsent(friend.getId(), id -> new HashSet<>());
        fFriendIds.add(user.getId());
        log.debug("Friends added: User = {}, friend = {}", user, friend);
    }

    @Override
    public void removeFromFriends(User user, User friend) {
        log.debug("Removing from friends: User = {}, friend = {}", user, friend);
        Set<Long> uFriendIds = userFriendIds.computeIfAbsent(user.getId(), id -> new HashSet<>());
        uFriendIds.remove(friend.getId());

        Set<Long> fFriendIds = userFriendIds.computeIfAbsent(friend.getId(), id -> new HashSet<>());
        fFriendIds.remove(user.getId());
        log.debug("Friends removed: User = {}, friend = {}", user, friend);
    }

    @Override
    public Collection<User> getMutualFriends(User user, User friend) {
        log.info("Get mutual friends: User = {}, friend = {}", user, friend);
        Set<Long> uFriendIds = userFriendIds.computeIfAbsent(user.getId(), id -> new HashSet<>());
        Set<Long> fFriendIds = userFriendIds.computeIfAbsent(friend.getId(), id -> new HashSet<>());
        List<User> mutualFriends = new ArrayList<>();
        for (Long uFriendId : uFriendIds) {
            if (fFriendIds.contains(uFriendId)) {
                mutualFriends.add(users.get(uFriendId));
            }
        }
        log.info("Mutual friends found: User = {}, friend = {}", user, friend);
        return mutualFriends;
    }

    @Override
    public Collection<User> getFriendsList(User user) {
        Set<Long> uFriendIds = userFriendIds.computeIfAbsent(user.getId(), id -> new HashSet<>());
        return uFriendIds.stream()
                .map(users::get)
                .collect(Collectors.toList());
    }

    private Long generateNextId() {
        return ++seq;
    }
}
