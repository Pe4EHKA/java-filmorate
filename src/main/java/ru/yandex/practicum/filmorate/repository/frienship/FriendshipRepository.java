package ru.yandex.practicum.filmorate.repository.frienship;

import java.util.Collection;

public interface FriendshipRepository {
    Collection<Long> getFriendInvitesToUser(long toUserId);

    Collection<Long> getMutualFriends(long userId, long friendId);

    void addToFriends(long userId, long toUserId, boolean accepted);

    void removeFromFriends(long userId, long toUserId);

    boolean containsInvite(long userId, long toUserId);
}
