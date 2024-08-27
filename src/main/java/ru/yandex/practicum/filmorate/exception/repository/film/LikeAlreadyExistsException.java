package ru.yandex.practicum.filmorate.exception.repository.film;

public class LikeAlreadyExistsException extends RuntimeException {
    public static final String LIKE_ALREADY_EXISTS = "Like to film with Id: %d from User with Id: %d already exists";

    public LikeAlreadyExistsException(String message) {
        super(message);
    }
}
