package ru.yandex.practicum.filmorate.repository.film_like;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.InternalServerException;
import ru.yandex.practicum.filmorate.model.Like;
import ru.yandex.practicum.filmorate.repository.BaseRepository;
import ru.yandex.practicum.filmorate.repository.mappers.FilmLikeRowMapper;

import java.util.Optional;

@Slf4j
@Repository
public class FilmLikeDbStorage extends BaseRepository<Like> implements FilmLikeRepository {
    private static final String INSERT_LIKE_QUERY = "INSERT INTO film_likes (film_id, user_id) VALUES (?, ?)";
    private static final String DELETE_LIKE_QUERY = "DELETE FROM film_likes WHERE film_id = ? AND user_id = ?";
    private static final String COUNT_LIKE_QUERY = "SELECT COUNT(*) FROM film_likes WHERE film_id = ?";
    private static final String CHECK_LIKE_QUERY = "SELECT film_id, user_id FROM film_likes " +
            "WHERE film_id = ? AND user_id = ?";

    public FilmLikeDbStorage(JdbcTemplate jdbcTemplate, FilmLikeRowMapper mapper) {
        super(jdbcTemplate, mapper);
    }

    @Override
    public void addLikeFilm(long filmId, long userId) {
        log.debug("Adding like film: {} from user {}", filmId, userId);
        jdbcTemplate.update(INSERT_LIKE_QUERY, filmId, userId);
        log.trace("Added like film: {} from user {}", filmId, userId);
    }

    @Override
    public void deleteLikeFilm(long filmId, long userId) {
        log.debug("Deleting like film: {} from user {}", filmId, userId);
        delete(DELETE_LIKE_QUERY, filmId, userId);
        log.trace("Deleted like film: {} from user {}", filmId, userId);
    }

    @Override
    public int countLikeFilm(long filmId) {
        log.debug("Counting likes film: {}", filmId);
        Integer count = jdbcTemplate.queryForObject(COUNT_LIKE_QUERY, Integer.class, filmId);
        if (count == null) {
            throw new InternalServerException("Не удалось посчитать кол-во лайков для фильма с ID: " + filmId);
        }
        return count;
    }

    @Override
    public boolean isLikeFilm(long filmId, long userId) {
        log.debug("Checking likes film: {} from user {}", filmId, userId);
        Optional<Like> like = findOne(CHECK_LIKE_QUERY, filmId, userId);
        return like.isPresent();
    }
}
