package ru.yandex.practicum.filmorate.repository.frienship;

import java.util.Collection;

public interface FriendshipRepository {
    Collection<Long> getFriendInvitesToUser(long toUserId);

    void addToFriends(long fromUserId, long toUserId, boolean accepted);

    void removeFromFriends(long fromUserId, long toUserId);

    boolean containsInvite(long fromUserId, long toUserId);
}
