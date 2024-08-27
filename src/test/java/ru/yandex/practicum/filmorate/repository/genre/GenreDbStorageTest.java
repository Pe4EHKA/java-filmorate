package ru.yandex.practicum.filmorate.repository.genre;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.repository.mappers.GenreRowMapper;

import java.util.Collection;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@JdbcTest
@AutoConfigureTestDatabase
@Import({GenreDbStorage.class, GenreRowMapper.class})
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@DisplayName("GenreDbStorageTest")
class GenreDbStorageTest {
    private final GenreDbStorage genreDbStorage;

    static Genre getGenreTest() {
        return Genre.builder()
                .id(1L)
                .name("Комедия")
                .build();
    }

    @Test
    @DisplayName("Getting all genres")
    void getAllGenres() {
        final Collection<Genre> genres = genreDbStorage.getAllGenres();

        assertNotNull(genres);
        assertFalse(genres.isEmpty());
        assertTrue(genres.contains(getGenreTest()));
    }

    @Test
    @DisplayName("Get exact genre")
    void getGenre() {
        final Genre genreTest = getGenreTest();
        final Optional<Genre> genreDb = genreDbStorage.getGenre(genreTest.getId());

        assertThat(genreDb)
                .isPresent()
                .hasValueSatisfying(genre -> assertThat(genre).hasFieldOrPropertyWithValue("id", 1L)
                );
        assertEquals(genreTest, genreDb.get());
    }

    @Test
    @DisplayName("Checking DB contains genre")
    void containsGenre() {
        assertTrue(genreDbStorage.containsGenre(getGenreTest().getId()));
    }
}