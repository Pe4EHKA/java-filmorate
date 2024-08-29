package ru.yandex.practicum.filmorate.exception.repository.genre;

public class GenreWrongNumberException extends RuntimeException {
    public static final String GENRE_WRONG_NUMBER_FOUND = "Genre with Id: %d does not exist";

    public GenreWrongNumberException(String message) {
        super(message);
    }
}
