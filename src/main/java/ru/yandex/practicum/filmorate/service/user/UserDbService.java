package ru.yandex.practicum.filmorate.service.user;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.controller.UserControllerException;
import ru.yandex.practicum.filmorate.exception.repository.friendship.FriendshipAlreadyExists;
import ru.yandex.practicum.filmorate.exception.repository.friendship.FriendshipNotFoundException;
import ru.yandex.practicum.filmorate.exception.repository.user.UserAlreadyExistsException;
import ru.yandex.practicum.filmorate.exception.repository.user.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.repository.frienship.FriendshipRepository;
import ru.yandex.practicum.filmorate.repository.user.UserRepository;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UserDbService implements UserService {
    private final UserRepository userRepository;
    private final FriendshipRepository friendshipRepository;

    @Autowired
    public UserDbService(@Qualifier("userDbStorage") UserRepository userRepository,
                         FriendshipRepository friendshipRepository) {
        this.userRepository = userRepository;
        this.friendshipRepository = friendshipRepository;
    }

    @Override
    public User createUser(User user) {
        checkUserBeforeCreate(user);
        return userRepository.createUser(user);
    }

    @Override
    public User updateUser(User user) {
        checkUserBeforeUpdate(user);
        return userRepository.updateUser(user);
    }

    @Override
    public User getUserById(long id) {
        if (!userRepository.containsUser(id)) {
            log.warn("User with id {} not found", id);
            throw new UserNotFoundException(String.format(UserNotFoundException.USER_NOT_FOUND, id));
        }
        return userRepository.getUserById(id)
                .orElseThrow(() -> new UserNotFoundException(String.format(UserNotFoundException.USER_NOT_FOUND, id)));
    }

    @Override
    public User addToFriends(long userId, long friendId) {
        checkFriendshipBeforeAdd(userId, friendId);
        boolean isMutual = friendshipRepository.containsInvite(friendId, userId);
        friendshipRepository.addToFriends(friendId, userId, isMutual);
        return userRepository.getUserById(friendId).orElseThrow(
                () -> new UserNotFoundException(String.format(UserNotFoundException.USER_NOT_FOUND, friendId)));
    }

    @Override
    public User removeFromFriends(long userId, long friendId) {
        checkFriendshipBeforeDelete(userId, friendId);
        friendshipRepository.removeFromFriends(friendId, userId);
        return userRepository.getUserById(friendId).orElseThrow(
                () -> new UserNotFoundException(String.format(UserNotFoundException.USER_NOT_FOUND, friendId)));
    }

    @Override
    public Collection<User> getAllUsers() {
        return userRepository.getAllUsers();
    }

    @Override
    public Collection<User> getMutualFriends(long userId, long friendId) {
        log.debug("Get mutual friends from user {} to friend {}", userId, friendId);
        checkMutualFriendsBeforeGet(userId, friendId);
        Collection<Long> friendListUserId = friendshipRepository.getFriendInvitesToUser(userId);
        Collection<Long> friendListFriendId = friendshipRepository.getFriendInvitesToUser(friendId);
        Set<Long> mutualFriends = friendListUserId.stream()
                .filter(friendListFriendId::contains)
                .collect(Collectors.toSet());
        return mutualFriends.stream()
                .map(userRepository::getUserById)
                .map(Optional::get)
                .collect(Collectors.toList());
    }

    @Override
    public Collection<User> getFriendsList(long userId) {
        if (!userRepository.containsUser(userId)) {
            log.warn("User with id {} not found", userId);
            throw new UserNotFoundException(String.format(UserNotFoundException.USER_NOT_FOUND, userId));
        }
        return friendshipRepository.getFriendInvitesToUser(userId).stream()
                .map(userRepository::getUserById)
                .map(Optional::get)
                .toList();
    }

    private void checkUserBeforeCreate(User user) {
        log.debug("Checking if user {} is already has id", user);
        String warnMessage = "Troubles with creating user";
        if (user.getId() != null) {
            log.warn(warnMessage);
            if (userRepository.containsUser(user.getId())) {
                throw new UserAlreadyExistsException(String
                        .format(UserAlreadyExistsException.USER_ALREADY_EXISTS, user.getId()));
            } else {
                throw new IllegalArgumentException("Устанавливать id пользователям самостоятельно запрещено");
            }
        }
    }

    private void checkUserBeforeUpdate(User user) {
        log.debug("Checking if user {} contains to update", user);
        if (!userRepository.containsUser(user.getId())) {
            log.warn("Troubles with updating user {}", String.format(UserNotFoundException.USER_NOT_FOUND, user.getId()));
            throw new UserNotFoundException(String.format(UserNotFoundException.USER_NOT_FOUND, user.getId()));
        }
    }

    private void checkFriendshipBeforeAdd(long userId, long friendId) {
        log.debug("CheckFriendship before adding user {} and friend {}", userId, friendId);
        String warnMessage = "Troubles with adding friendship";
        checkUsersExist(userId, friendId, warnMessage);
        if (friendshipRepository.containsInvite(friendId, userId)) {
            log.warn(warnMessage);
            throw new FriendshipAlreadyExists(String.format(FriendshipAlreadyExists.FRIENDSHIP_ALREADY_EXISTS,
                    friendId, userId));
        }
        if (userId == friendId) {
            log.warn(warnMessage);
            throw new UserControllerException(String.format(UserControllerException.ADD_YOURSELF, userId));
        }
    }

    private void checkFriendshipBeforeDelete(long userId, long friendId) {
        log.debug("CheckFriendship before deleting user {} and friend {}", userId, friendId);
        String warnMessage = "Troubles with deleting friendship";
        checkUsersExist(userId, friendId, warnMessage);
        if (userId == friendId) {
            log.warn(warnMessage);
            throw new UserControllerException(String.format(UserControllerException
                    .DELETE_YOURSELF_FROM_FRIENDS, userId));
        }
        if (!friendshipRepository.containsInvite(friendId, userId)) {
            log.warn(warnMessage);
            throw new FriendshipNotFoundException(String
                    .format(FriendshipNotFoundException.FRIENDSHIP_NOT_FOUND, friendId, userId));
        }
    }

    private void checkMutualFriendsBeforeGet(long userId, long friendId) {
        log.debug("CheckMutualFriends before get user {} and friend {}", userId, friendId);
        String warnMessage = "Troubles with returning mutual friendship";
        checkUsersExist(userId, friendId, warnMessage);
        if (userId == friendId) {
            log.warn(warnMessage);
            throw new UserControllerException(String.format(UserControllerException
                    .MUTUAL_FRIENDS_AMONG_YOURSELF, userId));
        }
    }

    private void checkUsersExist(long userId, long friendId, String warnMessage) {
        if (!userRepository.containsUser(userId)) {
            log.warn(warnMessage);
            throw new UserNotFoundException(String.format(UserNotFoundException.USER_NOT_FOUND, userId));
        }
        if (!userRepository.containsUser(friendId)) {
            log.warn(warnMessage);
            throw new UserNotFoundException(String.format(UserNotFoundException.USER_NOT_FOUND, friendId));
        }
    }
}
