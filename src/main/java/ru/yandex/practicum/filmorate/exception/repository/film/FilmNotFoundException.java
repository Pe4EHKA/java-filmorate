package ru.yandex.practicum.filmorate.exception.repository.film;

public class FilmNotFoundException extends RuntimeException {
    public static final String FILM_NOT_FOUND = "Film with Id: %d not found";

    public FilmNotFoundException(String message) {
        super(message);
    }
}
