package ru.yandex.practicum.filmorate.exception.repository.film;

public class LikeNotFoundException extends RuntimeException {
    public static final String LIKE_NOT_FOUND = "Like with Id: %d from User with Id: %d not found";

    public LikeNotFoundException(String message) {
        super(message);
    }
}
