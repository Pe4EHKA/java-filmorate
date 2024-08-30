package ru.yandex.practicum.filmorate.repository.frienship;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.filmorate.model.Friendship;
import ru.yandex.practicum.filmorate.repository.mappers.FriendshipRowMapper;

import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@JdbcTest
@AutoConfigureTestDatabase
@Import({FriendshipDbStorage.class, FriendshipRowMapper.class})
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@DisplayName("FriendshipDbStorageTest")
class FriendshipDbStorageTest {
    private final FriendshipDbStorage friendshipDbStorage;

    static Friendship getTestFriendship() {
        return Friendship.builder()
                .userId(3L)
                .friendId(2L)
                .accepted(true)
                .build();
    }

    @Test
    @DisplayName("Getting invites for friendship to exact user")
    void getFriendInvitesToUser() {
        Collection<Long> invitesToUser = List.of(1L);
        assertEquals(invitesToUser, friendshipDbStorage.getFriendInvitesToUser(2L));
    }

    @Test
    @DisplayName("Adding to friends")
    void addToFriends() {
        Friendship friendship = getTestFriendship();
        friendshipDbStorage
                .addToFriends(friendship.getUserId(), friendship.getFriendId());

        Collection<Long> invitesFriends = friendshipDbStorage.getFriendInvitesToUser(friendship.getFriendId());

        assertTrue(invitesFriends.contains(friendship.getUserId()));
    }

    @Test
    @DisplayName("Removing from friends")
    void removeFromFriends() {
        friendshipDbStorage.removeFromFriends(1L, 2L);

        Collection<Long> invitesFriends = friendshipDbStorage.getFriendInvitesToUser(2L);
        assertTrue(!invitesFriends.contains(2L));
    }
}