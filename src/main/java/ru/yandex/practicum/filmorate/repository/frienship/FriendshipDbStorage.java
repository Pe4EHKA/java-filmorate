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
    private static final String INSERT_QUERY = "INSERT INTO friendships (from_user_id, to_user_id, accepted) " +
            "VALUES (?, ?, ?)";
    private static final String DELETE_QUERY = "DELETE FROM friendships WHERE from_user_id = ? AND to_user_id = ?";
    private static final String FIND_BY_ID_QUERY = "SELECT from_user_id, to_user_id, accepted FROM friendships " +
            "WHERE from_user_id = ? AND to_user_id = ?";
    private static final String UPDATE_FRIENDSHIP_QUERY = "UPDATE friendships SET accepted=false " +
            "WHERE from_user_id = ? AND to_user_id = ?";
    private static final String FIND_INVITES_TO_USER_QUERY = "SELECT from_user_id, to_user_id, accepted " +
            "FROM friendships WHERE to_user_id = ?";

    public FriendshipDbStorage(JdbcTemplate jdbcTemplate, FriendshipRowMapper mapper) {
        super(jdbcTemplate, mapper);
    }

    @Override
    public Collection<Long> getFriendInvitesToUser(long toUserId) {
        log.debug("getFriendInvitesToUser: {}", toUserId);
        List<Long> friendInvitesToUserFrom = findMany(FIND_INVITES_TO_USER_QUERY, toUserId).stream()
                .map(Friendship::getFromUserId)
                .toList();
        log.trace("FriendInvitesToUser: user_id: {} {}", toUserId, friendInvitesToUserFrom);
        return friendInvitesToUserFrom;
    }

    @Override
    public void addToFriends(long fromUserId, long toUserId, boolean accepted) {
        log.debug("Adding to friends: {}, {}, {}", fromUserId, toUserId, accepted);
        jdbcTemplate
                .update(INSERT_QUERY, fromUserId, toUserId, accepted);
        Optional<Friendship> friendship = findOne(FIND_BY_ID_QUERY, fromUserId, toUserId);
        if (friendship.isEmpty()) {
            throw new InternalServerException("Ошибка в создании связи друзей с from_user_id: " + fromUserId
                    + " to_user_id: " + toUserId);
        }
        log.debug("Added to friends: {}, {}, {}", fromUserId, toUserId, accepted);
    }

    @Override
    public void removeFromFriends(long fromUserId, long toUserId) {
        log.debug("Removing from friends: {}, {}", fromUserId, toUserId);
        Optional<Friendship> optionalFriendship = findOne(FIND_BY_ID_QUERY,
                fromUserId, toUserId);
        if (optionalFriendship.isEmpty()) {
            throw new NotFoundException("Пользователей с ID from_user_id: " + fromUserId + " to_user_id: " + toUserId
                    + " не найдено");
        }
        delete(DELETE_QUERY, fromUserId, toUserId);
        if (optionalFriendship.get().getAccepted()) {
            update(UPDATE_FRIENDSHIP_QUERY, toUserId, fromUserId);
            log.debug("Friendship between {} and {} now is not mutual", toUserId, fromUserId);
        }
        log.trace("Removed from friends: {}, {}", fromUserId, toUserId);
    }

    @Override
    public boolean containsInvite(long fromUserId, long toUserId) {
        log.debug("Contains invite: {}, {}", fromUserId, toUserId);
        return findOne(FIND_BY_ID_QUERY, fromUserId, toUserId).isPresent();
    }
}
