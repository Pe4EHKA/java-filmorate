package ru.yandex.practicum.filmorate.service.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;

public interface UserService {
    User createUser(User user);

    User updateUser(User user);

    User getUserById(long id);

    User addToFriends(long userId, long friendId);

    User removeFromFriends(long userId, long friendId);

    Collection<User> getAllUsers();

    Collection<User> getMutualFriends(long userId, long friendId);

    Collection<User> getFriendsList(long userId);
}
