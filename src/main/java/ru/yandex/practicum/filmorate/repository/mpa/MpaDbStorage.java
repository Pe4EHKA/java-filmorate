package ru.yandex.practicum.filmorate.repository.mpa;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.repository.BaseRepository;
import ru.yandex.practicum.filmorate.repository.mappers.MpaRowMapper;

import java.util.Collection;
import java.util.Optional;

@Slf4j
@Repository
public class MpaDbStorage extends BaseRepository<Mpa> implements MpaRepository {
    private static final String FIND_BY_ID_QUERY = "SELECT id, name FROM mpa WHERE id = ?";
    private static final String FIND_ALL_QUERY = "SELECT * FROM mpa ORDER BY id";

    public MpaDbStorage(JdbcTemplate jdbcTemplate, MpaRowMapper mapper) {
        super(jdbcTemplate, mapper);
    }

    @Override
    public Collection<Mpa> getAllMpa() {
        log.debug("getAllMpa");
        return findMany(FIND_ALL_QUERY);
    }

    @Override
    public Optional<Mpa> getMpa(long mpaId) {
        log.debug("getMpa: {}", mpaId);
        return findOne(FIND_BY_ID_QUERY, mpaId);
    }

    @Override
    public boolean containsMpa(long mpaId) {
        log.debug("containsMpa: {}", mpaId);
        return findOne(FIND_BY_ID_QUERY, mpaId).isPresent();
    }
}
