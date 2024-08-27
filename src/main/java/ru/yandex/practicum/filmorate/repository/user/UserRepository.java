package ru.yandex.practicum.filmorate.repository.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.Optional;

public interface UserRepository {
    Collection<User> getAllUsers();

    Optional<User> getUserById(long id);

    User createUser(User user);

    User updateUser(User user);

    boolean containsUser(long userId);
}
