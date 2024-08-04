package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class FilmServiceImpl implements FilmService {

    private final UserStorage userStorage;
    private final FilmStorage filmStorage;


    @Override
    public Film getFilmById(long id) {
        log.debug("Get film by id: {}", id);
        return filmStorage.getFilmById(id);
    }

    @Override
    public Film createFilm(Film film) {
        log.debug("Create film: {}", film);
        return filmStorage.createFilm(film);
    }

    @Override
    public Film updateFilm(Film film) {
        log.debug("Update film: {}", film);
        return filmStorage.updateFilm(film);
    }

    @Override
    public void addLike(long filmId, long userId) {
        Film film = filmStorage.getFilmById(filmId);
        if (film == null) {
            throw new NotFoundException("Film with id " + filmId + " not found");
        }
        User user = userStorage.getUserById(userId);
        if (user == null) {
            throw new NotFoundException("User with id " + userId + " not found");
        }
        log.debug("Adding like to film {}, from user {}", film, user);
        filmStorage.addLike(film, user);
    }

    @Override
    public void removeLike(long filmId, long userId) {
        Film film = filmStorage.getFilmById(filmId);
        if (film == null) {
            throw new NotFoundException("Film with id " + filmId + " not found");
        }
        User user = userStorage.getUserById(userId);
        if (user == null) {
            throw new NotFoundException("User with id " + userId + " not found");
        }
        log.debug("Removing like from film {}, from user {}", film, user);
        filmStorage.removeLike(film, user);
        log.debug("Removed like from film {}, from user {}", film, user);
    }

    @Override
    public Collection<Film> getAllFilms() {
        log.debug("Get all films");
        return filmStorage.getAllFilms();
    }

    @Override
    public Collection<Film> getPopularFilms(int count) {
        log.debug("Get popular films");
        return filmStorage.getMostLikedFilms(count);
    }
}
