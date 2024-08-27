package ru.yandex.practicum.filmorate.exception.repository.genre;

public class GenreNotFoundException extends RuntimeException {
    public static final String GENRE_NOT_FOUND = "Genre with Id: %d not found";

    public GenreNotFoundException(String message) {
        super(message);
    }
}
