package ru.yandex.practicum.filmorate.repository.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.InternalServerException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.repository.BaseRepository;
import ru.yandex.practicum.filmorate.repository.mappers.FilmRowMapper;
import ru.yandex.practicum.filmorate.repository.mappers.GenreRowMapper;

import java.sql.Date;
import java.util.*;

@Slf4j
@Repository
public class FilmDbStorage extends BaseRepository<Film> implements FilmRepository {
    private static final String FIND_ALL_QUERY = "SELECT id, name, description, release_date, duration, " +
            "mpa_id FROM films";

    private static final String FIND_BY_ID_QUERY = "SELECT id, name, description, release_date, duration, " +
            "mpa_id FROM films WHERE id = ?";

    private static final String INSERT_FILM_QUERY = "INSERT INTO films (name, description, release_date, duration, " +
            "mpa_id) VALUES (?, ?, ?, ?, ?)";

    private static final String UPDATE_QUERY = "UPDATE films SET name=?, description=?, release_date=?, duration=?, " +
            "mpa_id=? WHERE id=?";

    private static final String FIND_FILM_GENRES_QUERY = "SELECT f.genre_id AS id, g.name " +
            "FROM film_genres AS f " +
            "LEFT OUTER JOIN genres AS g ON f.genre_id = g.id " +
            "WHERE f.film_id = ? " +
            "ORDER BY id";

    private static final String DELETE_FILM_GENRES_QUERY = "DELETE FROM film_genres WHERE film_id = ?";

    private static final String INSERT_FILM_GENRES_QUERY = "INSERT INTO film_genres (film_id, genre_id) VALUES (?, ?)";

    private final GenreRowMapper genreRowMapper;

    public FilmDbStorage(JdbcTemplate jdbcTemplate, FilmRowMapper mapper, GenreRowMapper genreRowMapper) {
        super(jdbcTemplate, mapper);
        this.genreRowMapper = genreRowMapper;
    }

    @Override
    public Collection<Film> getAllFilms() {
        log.debug("getAllFilms");
        return findMany(FIND_ALL_QUERY);
    }


    @Override
    public Optional<Film> getFilmById(long filmId) {
        log.debug("getFilmById {}", filmId);
        return findOne(FIND_BY_ID_QUERY, filmId);
    }

    @Override
    public Film createFilm(Film film) {
        log.debug("createFilm {}", film);
        long id = insert(
                INSERT_FILM_QUERY,
                film.getName(),
                film.getDescription(),
                Date.valueOf(film.getReleaseDate()),
                film.getDuration(),
                film.getMpa().getId()
        );
        Optional<Film> result = findOne(FIND_BY_ID_QUERY, id);
        if (result.isPresent()) {
            log.trace("Film created {}", film.getId());
            return result.get();
        } else {
            throw new InternalServerException("Не удалось сохранить фильм: " + film);
        }
    }

    @Override
    public Film updateFilm(Film film) {
        log.debug("updateFilm {}", film);
        update(
                UPDATE_QUERY,
                film.getName(),
                film.getDescription(),
                Date.valueOf(film.getReleaseDate()),
                film.getDuration(),
                film.getMpa().getId(),
                film.getId()
        );
        Optional<Film> result = findOne(FIND_BY_ID_QUERY, film.getId());
        if (result.isPresent()) {
            log.trace("Film updated {}", film);
            return result.get();
        } else {
            throw new InternalServerException("Не удалось обновить фильм: " + film);
        }
    }

    @Override
    public LinkedHashSet<Genre> getFilmGenres(long filmId) {
        log.debug("getFilmGenres {}", filmId);
        LinkedHashSet<Genre> genres = new LinkedHashSet<>(jdbcTemplate.query(FIND_FILM_GENRES_QUERY, genreRowMapper, filmId));
        log.trace("Returned {} genres", genres.size());
        return genres;
    }

    @Override
    public void addFilmGenres(long filmId, Collection<Genre> genres) {
        log.debug("addFilmGenres {}", filmId);
        if (genres != null && !genres.isEmpty()) {
            for (Genre genre : genres) {
                jdbcTemplate.update(INSERT_FILM_GENRES_QUERY, filmId, genre.getId());
                log.trace("Film Id {} added genre Id {}", filmId, genre.getId());
            }
            log.debug("Added {} genres to film {}", genres.size(), filmId);
        }
    }

    @Override
    public void removeFilmGenres(long filmId) {
        log.debug("removeFilmGenres {}", filmId);
        delete(DELETE_FILM_GENRES_QUERY, filmId);
        log.debug("removedFilmGenres {}", filmId);
    }

    @Override
    public void updateFilmGenres(long filmId, Collection<Genre> genres) {
        log.debug("updateFilmGenres {}", filmId);
        delete(DELETE_FILM_GENRES_QUERY, filmId);
        addFilmGenres(filmId, genres);
        log.debug("updated {} genres to film {}", genres.size(), filmId);
    }
}
