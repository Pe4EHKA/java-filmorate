package ru.yandex.practicum.filmorate.exception.repository.mpa;

public class MpaNotFoundException extends RuntimeException {
    public static final String MPA_NOT_FOUND = "Mpa with Id: %d not found";

    public MpaNotFoundException(String message) {
        super(message);
    }
}
