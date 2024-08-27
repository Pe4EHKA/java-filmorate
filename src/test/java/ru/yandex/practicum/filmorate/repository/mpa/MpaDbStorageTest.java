package ru.yandex.practicum.filmorate.repository.mpa;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.repository.mappers.MpaRowMapper;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

@JdbcTest
@AutoConfigureTestDatabase
@Import({MpaDbStorage.class, MpaRowMapper.class})
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@DisplayName("MpaDbStorageTest")
class MpaDbStorageTest {
    private final MpaDbStorage mpaDbStorage;

    static Mpa getMpaTest() {
        return Mpa.builder()
                .id(1L)
                .name("G")
                .build();
    }

    @Test
    @DisplayName("Getting all mpa")
    void getAllMpa() {
        Collection<Mpa> result = mpaDbStorage.getAllMpa();

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertTrue(result.contains(getMpaTest()));
    }

    @Test
    @DisplayName("Getting exact mpa")
    void getMpa() {
        Mpa mpaTest = getMpaTest();

        assertNotNull(mpaDbStorage.getMpa(mpaTest.getId()));
        assertEquals(mpaTest, mpaDbStorage.getMpa(mpaTest.getId()).get());
    }

    @Test
    @DisplayName("Checking DB contains mpa")
    void containsMpa() {
        assertTrue(mpaDbStorage.containsMpa(getMpaTest().getId()));
    }
}