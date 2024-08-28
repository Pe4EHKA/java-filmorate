package ru.yandex.practicum.filmorate.repository.frienship;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.InternalServerException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Friendship;
import ru.yandex.practicum.filmorate.repository.BaseRepository;
import ru.yandex.practicum.filmorate.repository.mappers.FriendshipRowMapper;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Slf4j
@Repository
public class FriendshipDbStorage extends BaseRepository<Friendship> implements FriendshipRepository {
    private static final String INSERT_QUERY = "INSERT INTO friendships (USER_ID, FRIEND_ID, accepted) " +
            "VALUES (?, ?, ?)";
    private static final String DELETE_QUERY = "DELETE FROM friendships WHERE USER_ID = ? AND FRIEND_ID = ?";
    private static final String FIND_BY_ID_QUERY = "SELECT USER_ID, FRIEND_ID, accepted FROM friendships " +
            "WHERE USER_ID = ? AND FRIEND_ID = ?";
    private static final String UPDATE_FRIENDSHIP_QUERY = "UPDATE friendships SET accepted=false " +
            "WHERE USER_ID = ? AND FRIEND_ID = ?";
    private static final String FIND_INVITES_TO_USER_QUERY = "SELECT USER_ID, FRIEND_ID, accepted " +
            "FROM friendships WHERE FRIEND_ID = ?";
    private static final String FIND_MUTUAL_FRIENDS_QUERY = "SELECT f1.USER_ID FROM friendships AS f1 " +
            "JOIN friendships AS f2 ON f1.USER_ID = f2.USER_ID WHERE f1.FRIEND_ID = ? AND f2.FRIEND_ID = ?";

    public FriendshipDbStorage(JdbcTemplate jdbcTemplate, FriendshipRowMapper mapper) {
        super(jdbcTemplate, mapper);
    }

    @Override
    public Collection<Long> getFriendInvitesToUser(long friendId) {
        log.debug("getFriendInvitesToUser: {}", friendId);
        List<Long> friendInvitesToUserFrom = findMany(FIND_INVITES_TO_USER_QUERY, friendId).stream()
                .map(Friendship::getUserId)
                .toList();
        log.trace("FriendInvitesToUser: user_id: {} {}", friendId, friendInvitesToUserFrom);
        return friendInvitesToUserFrom;
    }

    @Override
    public Collection<Long> getMutualFriends(long userId, long friendId) {
        log.debug("getMutualFriends: {}", userId);
        return jdbcTemplate.query(FIND_MUTUAL_FRIENDS_QUERY,
                (resultSet, rowNum) -> resultSet.getLong("user_id"), userId, friendId);
    }

    @Override
    public void addToFriends(long userId, long friendId, boolean accepted) {
        log.debug("Adding to friends: {}, {}, {}", userId, friendId, accepted);
        jdbcTemplate
                .update(INSERT_QUERY, userId, friendId, accepted);
        Optional<Friendship> friendship = findOne(FIND_BY_ID_QUERY, userId, friendId);
        if (friendship.isEmpty()) {
            throw new InternalServerException("Ошибка в создании связи друзей с USER_ID: " + userId
                    + " FRIEND_ID: " + friendId);
        }
        log.debug("Added to friends: {}, {}, {}", userId, friendId, accepted);
    }

    @Override
    public void removeFromFriends(long userId, long friendId) {
        log.debug("Removing from friends: {}, {}", userId, friendId);
        Optional<Friendship> optionalFriendship = findOne(FIND_BY_ID_QUERY,
                userId, friendId);
        if (optionalFriendship.isEmpty()) {
            throw new NotFoundException("Пользователей с ID USER_ID: " + userId + " FRIEND_ID: " + friendId
                    + " не найдено");
        }
        delete(DELETE_QUERY, userId, friendId);
        if (optionalFriendship.get().getAccepted()) {
            update(UPDATE_FRIENDSHIP_QUERY, friendId, userId);
            log.debug("Friendship between {} and {} now is not mutual", friendId, userId);
        }
        log.trace("Removed from friends: {}, {}", userId, friendId);
    }

    @Override
    public boolean containsInvite(long userId, long friendId) {
        log.debug("Contains invite: {}, {}", userId, friendId);
        return findOne(FIND_BY_ID_QUERY, userId, friendId).isPresent();
    }
}
