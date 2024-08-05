package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.Optional;

public interface FilmStorage {
    Collection<Film> getAllFilms();

    Optional<Film> getFilmById(long id);

    Film createFilm(Film film);

    Film updateFilm(Film film);

    void addLike(Film film, User user);

    void removeLike(Film film, User user);

    Collection<Film> getMostLikedFilms(int count);
}