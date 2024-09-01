package ru.yandex.practicum.filmorate.exception.repository.user;

public class UserNotFoundException extends RuntimeException {
    public static final String USER_NOT_FOUND = "User with Id: %d not found";

    public UserNotFoundException(String message) {
        super(message);
    }
}
