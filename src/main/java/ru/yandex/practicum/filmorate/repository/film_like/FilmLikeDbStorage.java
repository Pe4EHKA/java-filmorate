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
    public void addLikeFilm(long film_id, long user_id) {
        log.debug("Adding like film: {} from user {}", film_id, user_id);
        jdbcTemplate.update(INSERT_LIKE_QUERY, film_id, user_id);
        log.trace("Added like film: {} from user {}", film_id, user_id);
    }

    @Override
    public void deleteLikeFilm(long film_id, long user_id) {
        log.debug("Deleting like film: {} from user {}", film_id, user_id);
        delete(DELETE_LIKE_QUERY, film_id, user_id);
        log.trace("Deleted like film: {} from user {}", film_id, user_id);
    }

    @Override
    public int countLikeFilm(long film_id) {
        log.debug("Counting likes film: {}", film_id);
        Integer count = jdbcTemplate.queryForObject(COUNT_LIKE_QUERY, Integer.class, film_id);
        if (count == null) {
            throw new InternalServerException("Не удалось посчитать кол-во лайков для фильма с ID: " + film_id);
        }
        return count;
    }

    @Override
    public boolean isLikeFilm(long film_id, long user_id) {
        log.debug("Checking likes film: {} from user {}", film_id, user_id);
        Optional<Like> like = findOne(CHECK_LIKE_QUERY, film_id, user_id);
        return like.isPresent();
    }
}
