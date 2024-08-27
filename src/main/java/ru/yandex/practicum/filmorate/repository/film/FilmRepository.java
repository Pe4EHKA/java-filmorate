package ru.yandex.practicum.filmorate.repository.film;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Optional;

public interface FilmRepository {
    Collection<Film> getAllFilms();

    Optional<Film> getFilmById(long filmId);

    Film createFilm(Film film);

    Film updateFilm(Film film);

    LinkedHashSet<Genre> getFilmGenres(long filmId);

    void addFilmGenres(long filmId, Collection<Genre> genres);

    void removeFilmGenres(long filmId);

    void updateFilmGenres(long filmId, Collection<Genre> genres);
}