package ru.yandex.practicum.filmorate.exception.repository.user;

public class UserAlreadyExistsException extends RuntimeException {
    public static final String USER_ALREADY_EXISTS = "User with Id %d already exists";
    public static final String USER_DOESNT_EXIST = "User with Id %d does not exist";


    public UserAlreadyExistsException(String message) {
        super(message);
    }
}
