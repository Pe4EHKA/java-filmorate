package ru.yandex.practicum.filmorate.repository.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.InternalServerException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.repository.BaseRepository;
import ru.yandex.practicum.filmorate.repository.mappers.FilmRowMapper;

import java.sql.Date;
import java.util.*;

@Slf4j
@Repository
public class FilmDbStorage extends BaseRepository<Film> implements FilmRepository {
    private static final String FIND_ALL_QUERY = "SELECT * FROM FILMS f, MPA m WHERE f.MPA_ID = m.MPA_ID";

    private static final String FIND_BY_ID_QUERY = "SELECT * FROM FILMS f, MPA m WHERE f.MPA_ID = m.MPA_ID AND f.id = ?";

    private static final String INSERT_FILM_QUERY = "INSERT INTO films (name, description, release_date, duration, " +
            "mpa_id) VALUES (?, ?, ?, ?, ?)";

    private static final String UPDATE_QUERY = "UPDATE films SET name=?, description=?, release_date=?, duration=?, " +
            "mpa_id=? WHERE id=?";

    private static final String FIND_FILM_GENRES_QUERY = "SELECT * FROM GENRES g, FILM_GENRES fg " +
            "WHERE fg.GENRE_ID = g.GENRE_ID AND fg.FILM_ID IN (:filmIds) ORDER BY g.GENRE_ID";

    private static final String DELETE_FILM_GENRES_QUERY = "DELETE FROM film_genres WHERE film_id = ?";

    private static final String INSERT_FILM_GENRES_QUERY = "INSERT INTO film_genres (film_id, genre_id) VALUES (?, ?)";

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public FilmDbStorage(JdbcTemplate jdbcTemplate,
                         FilmRowMapper mapper,
                         NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        super(jdbcTemplate, mapper);
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
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
        if (film.getGenres() != null) {
            updateFilmGenres(film.getId(), film.getGenres());
        }
        Optional<Film> result = getFilmById(film.getId());
        if (result.isPresent()) {
            log.trace("Film updated {}", film);
            return result.get();
        } else {
            throw new InternalServerException("Не удалось обновить фильм: " + film);
        }
    }

    @Override
    public Map<Long, LinkedHashSet<Genre>> getFilmGenres(List<Long> filmIds) {
        log.debug("getFilmGenres {}", filmIds);
        if (filmIds == null || filmIds.isEmpty()) {
            return Collections.emptyMap();
        }
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("filmIds", filmIds);

        List<Map<String, Object>> resultRows = namedParameterJdbcTemplate.queryForList(FIND_FILM_GENRES_QUERY, params);
        Map<Long, LinkedHashSet<Genre>> genresMap = new HashMap<>();
        for (Map<String, Object> row : resultRows) {
            Long filmId = (Long) row.get("film_id");
            Long genreId = (Long) row.get("genre_id");
            String genreName = (String) row.get("genre_name");

            Genre genre = Genre.builder()
                    .id(genreId)
                    .name(genreName)
                    .build();
            genresMap.computeIfAbsent(filmId, k -> new LinkedHashSet<>()).add(genre);
        }
        log.trace("Returned {} genres", genresMap.keySet().size());
        return genresMap;
    }

    @Override
    public void addFilmGenres(long filmId, Collection<Genre> genres) {
        log.debug("addFilmGenres {}", filmId);
        if (genres != null && !genres.isEmpty()) {
            List<Object[]> params = new ArrayList<>(genres.size());
            for (Genre genre : genres) {
                params.add(new Object[]{filmId, genre.getId()});
                log.trace("Film Id {} added genre Id {}", filmId, genre.getId());
            }
            jdbcTemplate.batchUpdate(INSERT_FILM_GENRES_QUERY, params);
            log.debug("Added {} genres to film {}", genres.size(), filmId);
        }
    }

    private void updateFilmGenres(long filmId, Collection<Genre> genres) {
        log.debug("updateFilmGenres {}", filmId);
        delete(DELETE_FILM_GENRES_QUERY, filmId);
        addFilmGenres(filmId, genres);
        log.debug("updated {} genres to film {}", genres.size(), filmId);
    }
}
