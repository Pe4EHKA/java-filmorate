package ru.yandex.practicum.filmorate.repository.genre;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.repository.BaseRepository;
import ru.yandex.practicum.filmorate.repository.mappers.GenreRowMapper;

import java.util.Collection;
import java.util.Optional;

@Slf4j
@Repository
public class GenreDbStorage extends BaseRepository<Genre> implements GenreRepository {
    private static final String FIND_BY_ID_QUERY = "SELECT * FROM genres WHERE id = ?";
    private static final String FIND_ALL_QUERY = "SELECT * FROM genres ORDER BY id";

    public GenreDbStorage(JdbcTemplate jdbcTemplate, GenreRowMapper mapper) {
        super(jdbcTemplate, mapper);
    }

    @Override
    public Collection<Genre> getAllGenres() {
        log.debug("getAllGenres");
        return findMany(FIND_ALL_QUERY);
    }

    @Override
    public Optional<Genre> getGenre(long genreId) {
        log.debug("getGenre {}", genreId);
        return findOne(FIND_BY_ID_QUERY, genreId);
    }

    @Override
    public boolean containsGenre(long genreId) {
        log.debug("containsGenre {}", genreId);
        return findOne(FIND_BY_ID_QUERY, genreId).isPresent();
    }
}
