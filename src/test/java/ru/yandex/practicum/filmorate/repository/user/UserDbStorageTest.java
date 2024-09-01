package ru.yandex.practicum.filmorate.repository.user;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.repository.mappers.UserRowMapper;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@JdbcTest
@AutoConfigureTestDatabase
@Import({UserDbStorage.class, UserRowMapper.class})
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@DisplayName("UserDbStorageTest")
class UserDbStorageTest {
    private static final long TEST_USER_ID1 = 1L;
    private static final long TEST_USER_ID2 = 2L;
    private static final long TEST_USER_ID3 = 3L;
    private final UserDbStorage userDbStorage;

    static User getTestUser1() {
        return User.builder()
                .id(TEST_USER_ID1)
                .email("email1@email.com")
                .login("login1")
                .name("name1")
                .birthday(LocalDate.of(1999, 9, 9))
                .build();
    }

    static User getTestUser2() {
        return User.builder()
                .id(TEST_USER_ID2)
                .email("email2@email.com")
                .login("login2")
                .name("name2")
                .birthday(LocalDate.of(1999, 9, 9))
                .build();
    }

    static User getTestUser3() {
        return User.builder()
                .id(TEST_USER_ID3)
                .email("email3@email.com")
                .login("login3")
                .name("name3")
                .birthday(LocalDate.of(1999, 9, 9))
                .build();
    }

    static Collection<User> getTestUsers() {
        return List.of(getTestUser1(), getTestUser2(), getTestUser3());
    }

    @Test
    @DisplayName("Getting all users")
    void shouldGetAllUsers() {
        final Collection<User> users = userDbStorage.getAllUsers();
        Collection<User> userInit = getTestUsers();
        assertEquals(users, userInit);
    }

    @Test
    @DisplayName("Getting user by id")
    void shouldGetUserById() {
        Optional<User> userOptional = userDbStorage.getUserById(TEST_USER_ID1);

        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(user -> assertThat(user).hasFieldOrPropertyWithValue("id", TEST_USER_ID1)
                );
        assertEquals(getTestUser1(), userOptional.get());
    }

    @Test
    @DisplayName("Creating user")
    void shouldCreateUser() {
        User userTest = User.builder()
                .email("email4@email.com")
                .login("login4")
                .name("name4")
                .birthday(LocalDate.of(1999, 9, 9))
                .build();

        User userOptional = userDbStorage.createUser(userTest);
        assertThat(userDbStorage.getUserById(userOptional.getId()))
                .isPresent()
                .hasValueSatisfying(user -> assertThat(user).hasFieldOrPropertyWithValue("id", 4L)
                );

        userTest.setId(4L);
        assertEquals(userTest, userOptional);

    }

    @Test
    @DisplayName("Updating user")
    void shouldUpdateUser() {
        User userTest = getTestUser3();
        userTest.setEmail("em@em.ru");
        userDbStorage.updateUser(userTest);

        Optional<User> userOptional = userDbStorage.getUserById(userTest.getId());
        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(user -> assertThat(user).hasFieldOrPropertyWithValue("id", 3L)
                );

        assertEquals(userTest, userOptional.get());
    }

    @Test
    @DisplayName("Checking DB contains user")
    void shouldContainsUser() {
        assertTrue(userDbStorage.containsUser(TEST_USER_ID1));
    }
}