package ru.yandex.practicum.filmorate.service.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.repository.film.FilmAlreadyExistsException;
import ru.yandex.practicum.filmorate.exception.repository.film.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exception.repository.film.LikeAlreadyExistsException;
import ru.yandex.practicum.filmorate.exception.repository.film.LikeNotFoundException;
import ru.yandex.practicum.filmorate.exception.repository.genre.GenreNotFoundException;
import ru.yandex.practicum.filmorate.exception.repository.mpa.MpaNotFoundException;
import ru.yandex.practicum.filmorate.exception.repository.mpa.MpaWrongNumberException;
import ru.yandex.practicum.filmorate.exception.repository.user.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.repository.film.FilmRepository;
import ru.yandex.practicum.filmorate.repository.film_like.FilmLikeRepository;
import ru.yandex.practicum.filmorate.repository.genre.GenreRepository;
import ru.yandex.practicum.filmorate.repository.mpa.MpaRepository;
import ru.yandex.practicum.filmorate.repository.user.UserRepository;

import java.util.Collection;

@Slf4j
@Service
public class FilmServiceImpl implements FilmService {
    private final FilmRepository filmRepository;
    private final UserRepository userRepository;
    private final GenreRepository genreRepository;
    private final FilmLikeRepository filmLikeRepository;
    private final MpaRepository mpaRepository;

    @Autowired
    public FilmServiceImpl(@Qualifier("filmDbStorage") FilmRepository filmRepository,
                           @Qualifier("userDbStorage") UserRepository userRepository,
                           GenreRepository genreRepository,
                           FilmLikeRepository filmLikeRepository,
                           MpaRepository mpaRepository) {
        this.filmRepository = filmRepository;
        this.userRepository = userRepository;
        this.genreRepository = genreRepository;
        this.filmLikeRepository = filmLikeRepository;
        this.mpaRepository = mpaRepository;
    }

    @Override
    public Film getFilmById(long id) {
        Film film = filmRepository.getFilmById(id)
                .orElseThrow(() -> new FilmNotFoundException(String.format(FilmNotFoundException.FILM_NOT_FOUND, id)));
        film.setGenres(filmRepository.getFilmGenres(id));
        film.setMpa(mpaRepository.getMpa(film.getMpa().getId())
                .orElseThrow(() -> new MpaNotFoundException(String
                        .format(MpaNotFoundException.MPA_NOT_FOUND, film.getMpa().getId()))));
        return film;
    }

    @Override
    public Film createFilm(Film film) {
        checkFilmBeforeAdd(film);
        Film result = filmRepository.createFilm(film);
        filmRepository.addFilmGenres(result.getId(), film.getGenres());
        result.setMpa(mpaRepository.getMpa(film.getMpa().getId())
                .orElseThrow(() -> new MpaNotFoundException(String
                        .format(MpaNotFoundException.MPA_NOT_FOUND, film.getMpa().getId()))));
        result.setGenres(filmRepository.getFilmGenres(result.getId()));
        return result;
    }

    @Override
    public Film updateFilm(Film film) {
        checkFilmBeforeUpdate(film);
        Film result = filmRepository.updateFilm(film);
        result.setMpa(mpaRepository.getMpa(result.getMpa().getId()).orElseThrow(() -> new MpaNotFoundException(String
                .format(MpaNotFoundException.MPA_NOT_FOUND, film.getMpa().getId()))));
        result.setGenres(filmRepository.getFilmGenres(result.getId()));
        return result;
    }

    @Override
    public void addLike(long filmId, long userId) {
        checkLikeBeforeAdd(filmId, userId);
        filmLikeRepository.addLikeFilm(filmId, userId);
    }

    @Override
    public void removeLike(long filmId, long userId) {
        checkLikeBeforeDelete(filmId, userId);
        filmLikeRepository.deleteLikeFilm(filmId, userId);
    }

    @Override
    public Collection<Film> getAllFilms() {
        log.debug("getAllFilms");
        Collection<Film> films = filmRepository.getAllFilms();
        for (Film film : films) {
            film.setMpa(mpaRepository.getMpa(film.getMpa().getId()).orElseThrow(() -> new MpaNotFoundException(String
                    .format(MpaNotFoundException.MPA_NOT_FOUND, film.getMpa().getId()))));
            film.setGenres(filmRepository.getFilmGenres(film.getId()));
        }
        return films;
    }

    @Override
    public Collection<Film> getPopularFilms(int count) {
        log.debug("getPopularFilms");
        return filmRepository.getAllFilms().stream()
                .sorted(this::compareLikes)
                .limit(count)
                .toList();
    }

    private Integer compareLikes(Film film, Film toCompareFilm) {
        return Integer.compare(filmLikeRepository.countLikeFilm(toCompareFilm.getId()),
                filmLikeRepository.countLikeFilm(film.getId()));
    }

    private void checkFilmBeforeAdd(Film film) {
        log.debug("Checking film before adding: {}", film);
        String warnMessage = "Troubles with adding film";
        if (film.getId() != null) {
            log.warn(warnMessage);
            if (filmRepository.getFilmById(film.getId()).isPresent()) {
                throw new FilmAlreadyExistsException(
                        String.format(FilmAlreadyExistsException.FILM_ALREADY_EXISTS, film.getId()));
            } else {
                throw new IllegalArgumentException("Устанавливать id фильмам самостоятельно запрещено");
            }
        }
        if (!mpaRepository.containsMpa(film.getMpa().getId())) {
            log.warn(warnMessage);
            throw new MpaWrongNumberException(String.format(MpaNotFoundException.MPA_NOT_FOUND, film.getMpa().getId()));
        }
        for (Genre genre : genreRepository.getAllGenres()) {
            if (!genreRepository.containsGenre(genre.getId())) {
                log.warn(warnMessage);
                throw new GenreNotFoundException(String.format(GenreNotFoundException.GENRE_NOT_FOUND, genre.getId()));
            }
        }
    }

    private void checkFilmBeforeUpdate(Film film) {
        log.debug("Checking film before updating: {}", film);
        String warnMessage = "Troubles with updating film";
        if (filmRepository.getFilmById(film.getId()).isEmpty()) {
            log.warn(warnMessage);
            throw new FilmNotFoundException(String.format(FilmNotFoundException.FILM_NOT_FOUND, film.getId()));
        }
        if (!mpaRepository.containsMpa(film.getMpa().getId())) {
            log.warn(warnMessage);
            throw new MpaNotFoundException(String.format(MpaNotFoundException.MPA_NOT_FOUND, film.getMpa().getId()));
        }
        if (film.getGenres() != null && !film.getGenres().isEmpty()) {
            for (Genre genre : film.getGenres()) {
                if (!genreRepository.containsGenre(genre.getId())) {
                    log.warn(warnMessage);
                    throw new GenreNotFoundException(String.format(GenreNotFoundException.GENRE_NOT_FOUND, genre.getId()));
                }
            }
        }
    }

    private void checkLikeBeforeAdd(long filmId, long userId) {
        log.debug("Checking from User: {} like before adding to film: {}", userId, filmId);
        String warnMessage = "Troubles with adding like to film";
        checkFilmUserExists(filmId, userId, warnMessage);
        if (filmLikeRepository.isLikeFilm(filmId, userId)) {
            log.warn(warnMessage);
            throw new LikeAlreadyExistsException(String
                    .format(LikeAlreadyExistsException.LIKE_ALREADY_EXISTS, filmId, userId));
        }
    }

    private void checkLikeBeforeDelete(long filmId, long userId) {
        log.debug("Checking from User: {} like before delete from film: {}", userId, filmId);
        String warnMessage = "Troubles with deleting like from film";
        checkFilmUserExists(filmId, userId, warnMessage);
        if (!filmLikeRepository.isLikeFilm(filmId, userId)) {
            log.warn(warnMessage);
            throw new LikeNotFoundException(String
                    .format(LikeNotFoundException.LIKE_NOT_FOUND, filmId, userId));
        }
    }

    private void checkFilmUserExists(long filmId, long userId, String warnMessage) {
        if (filmRepository.getFilmById(filmId).isEmpty()) {
            log.warn(warnMessage);
            throw new FilmNotFoundException(String.format(FilmNotFoundException.FILM_NOT_FOUND, filmId));
        }
        if (!userRepository.containsUser(userId)) {
            log.warn(warnMessage);
            throw new UserNotFoundException(String.format(UserNotFoundException.USER_NOT_FOUND, userId));
        }
    }
}
