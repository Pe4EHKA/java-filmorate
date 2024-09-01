package ru.yandex.practicum.filmorate.service.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;

public interface FilmService {
    Film getFilmById(long id);

    Film createFilm(Film film);

    Film updateFilm(Film film);

    void addLike(long filmId, long userId);

    void removeLike(long filmId, long userId);

    Collection<Film> getAllFilms();

    Collection<Film> getPopularFilms(int count);
}
