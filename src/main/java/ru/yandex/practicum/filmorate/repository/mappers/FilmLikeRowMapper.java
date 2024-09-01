package ru.yandex.practicum.filmorate.repository.mappers;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Like;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class FilmLikeRowMapper implements RowMapper<Like> {
    @Override
    public Like mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        return Like.builder()
                .filmId(resultSet.getLong("film_id"))
                .userId(resultSet.getLong("user_id"))
                .build();
    }
}
