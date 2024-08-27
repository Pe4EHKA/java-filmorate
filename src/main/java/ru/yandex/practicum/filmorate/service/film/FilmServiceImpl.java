package ru.yandex.practicum.filmorate.service.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.repository.film.FilmRepository;
import ru.yandex.practicum.filmorate.repository.user.UserRepository;

import java.util.Collection;
import java.util.List;

@Service
@Slf4j
public class FilmServiceImpl implements FilmService {
    private final UserRepository userRepository;
    private final FilmRepository filmRepository;

    FilmServiceImpl(@Qualifier("inMemoryUserRepository") UserRepository userRepository,
                    @Qualifier("inMemoryFilmRepository") FilmRepository filmRepository) {
        this.userRepository = userRepository;
        this.filmRepository = filmRepository;
    }

    @Override
    public Film getFilmById(long id) {
        log.debug("Get film by id: {}", id);
        return filmRepository.getFilmById(id)
                .orElseThrow(() -> new NotFoundException("Film with id: %s not found".formatted(id)));
    }

    @Override
    public Film createFilm(Film film) {
        log.debug("Create film: {}", film);
        return filmRepository.createFilm(film);
    }

    @Override
    public Film updateFilm(Film film) {
        log.debug("Update film: {}", film);
        return filmRepository.updateFilm(film);
    }

    @Override
    public void addLike(long filmId, long userId) {
        Film film = filmRepository.getFilmById(filmId)
                .orElseThrow(() -> new NotFoundException("Film with id " + filmId + " not found"));
        User user = userRepository.getUserById(userId)
                .orElseThrow(() -> new NotFoundException("User with id " + userId + " not found"));
        log.debug("Adding like to film {}, from user {}", film, user);
//        filmRepository.addLike(film, user);
    }

    @Override
    public void removeLike(long filmId, long userId) {
        Film film = filmRepository.getFilmById(filmId)
                .orElseThrow(() -> new NotFoundException("Film with id " + filmId + " not found"));
        User user = userRepository.getUserById(userId)
                .orElseThrow(() -> new NotFoundException("User with id " + userId + " not found"));
        log.debug("Removing like from film {}, from user {}", film, user);
//        filmRepository.removeLike(film, user);
        log.debug("Removed like from film {}, from user {}", film, user);
    }

    @Override
    public Collection<Film> getAllFilms() {
        log.debug("Get all films");
        return filmRepository.getAllFilms();
    }

    @Override
    public Collection<Film> getPopularFilms(int count) {
        log.debug("Get popular films");
//        return filmRepository.getMostLikedFilms(count);
        return List.of();
    }
}
