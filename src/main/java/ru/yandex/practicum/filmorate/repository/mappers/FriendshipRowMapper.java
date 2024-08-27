package ru.yandex.practicum.filmorate.repository.mappers;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Friendship;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class FriendshipRowMapper implements RowMapper<Friendship> {
    @Override
    public Friendship mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        return Friendship.builder()
                .fromUserId(resultSet.getLong("from_user_id"))
                .toUserId(resultSet.getLong("to_user_id"))
                .accepted(resultSet.getBoolean("accepted"))
                .build();
    }
}
