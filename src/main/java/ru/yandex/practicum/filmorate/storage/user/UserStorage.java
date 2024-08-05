package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.Optional;

public interface UserStorage {
    Collection<User> getAllUsers();

    Optional<User> getUserById(long id);

    User createUser(User user);

    User updateUser(User user);

    void addToFriends(User user, User friend);

    void removeFromFriends(User user, User friend);

    Collection<User> getMutualFriends(User user, User friend);

    Collection<User> getFriendsList(User user);
}
