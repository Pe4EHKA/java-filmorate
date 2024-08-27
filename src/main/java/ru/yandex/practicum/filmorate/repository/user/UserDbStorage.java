package ru.yandex.practicum.filmorate.repository.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.InternalServerException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.repository.BaseRepository;
import ru.yandex.practicum.filmorate.repository.mappers.UserRowMapper;

import java.sql.Date;
import java.util.Collection;
import java.util.Optional;

@Slf4j
@Repository
public class UserDbStorage extends BaseRepository<User> implements UserRepository {
    private static final String FIND_ALL_QUERY = "SELECT * FROM users";
    private static final String FIND_BY_ID_QUERY = "SELECT * FROM users WHERE id = ?";
    private static final String FIND_BY_EMAIL_QUERY = "SELECT id, email, login, name, birthday FROM users WHERE email = ?";
    private static final String INSERT_QUERY = "INSERT INTO users (email, login, name, birthday) " +
            "VALUES (?, ?, ?, ?)";
    private static final String UPDATE_QUERY =
            "UPDATE users SET email = ?, login = ?, name = ?, birthday = ? WHERE id = ?";

    public UserDbStorage(JdbcTemplate jdbcTemplate, UserRowMapper mapper) {
        super(jdbcTemplate, mapper);
    }

    @Override
    public Collection<User> getAllUsers() {
        log.debug("FIND_ALL_USERS {}", FIND_ALL_QUERY);
        return findMany(FIND_ALL_QUERY);
    }

    @Override
    public Optional<User> getUserById(long id) {
        log.debug("FIND_BY_ID : {}, QUERY : {}", id, FIND_BY_ID_QUERY);
        return findOne(FIND_BY_ID_QUERY, id);
    }

    @Override
    public User createUser(User user) {
        log.debug("INSERT_USER : {}, QUERY :  {}", user, INSERT_QUERY);
        long id = insert(
                INSERT_QUERY,
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                Date.valueOf(user.getBirthday())
        );
        Optional<User> result = findOne(FIND_BY_EMAIL_QUERY, user.getEmail());
        if (result.isPresent()) {
            log.trace("User created : {}", result);
            return result.get();
        } else {
            throw new InternalServerException("Не удалось сохранить пользователя User: " + user);
        }
    }

    @Override
    public User updateUser(User user) {
        log.debug("UPDATE_USER : {}, QUERY :  {}", user, UPDATE_QUERY);
        update(
                UPDATE_QUERY,
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                Date.valueOf(user.getBirthday()),
                user.getId()
        );
        Optional<User> result = findOne(FIND_BY_ID_QUERY, user.getId());
        if (result.isPresent()) {
            log.trace("User updated : {}", result);
            return result.get();
        } else {
            throw new InternalServerException("Не удалось обновить пользователя с ID: " + user.getId());
        }
    }

    @Override
    public boolean containsUser(long userId) {
        return findOne(FIND_BY_ID_QUERY, userId).isPresent();
    }
}
