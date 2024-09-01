package ru.yandex.practicum.filmorate.repository.film;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.*;

public interface FilmRepository {
    Collection<Film> getAllFilms();

    Optional<Film> getFilmById(long filmId);

    Film createFilm(Film film);

    Film updateFilm(Film film);

    Map<Long, LinkedHashSet<Genre>> getFilmGenres(List<Long> filmIds);

    void addFilmGenres(long filmId, Collection<Genre> genres);
}