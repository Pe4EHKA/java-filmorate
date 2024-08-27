package ru.yandex.practicum.filmorate.exception.repository.film;

public class FilmAlreadyExistsException extends RuntimeException {
    public static final String FILM_ALREADY_EXISTS = "Film with Id: %d already exists";

    public FilmAlreadyExistsException(String message) {
        super(message);
    }
}
