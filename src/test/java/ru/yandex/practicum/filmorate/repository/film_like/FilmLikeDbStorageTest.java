package ru.yandex.practicum.filmorate.repository.film_like;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.filmorate.model.Like;
import ru.yandex.practicum.filmorate.repository.mappers.FilmLikeRowMapper;

import static org.junit.jupiter.api.Assertions.*;

@JdbcTest
@AutoConfigureTestDatabase
@Import({FilmLikeDbStorage.class, FilmLikeRowMapper.class})
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@DisplayName("FilmLikeDbStorageTest")
class FilmLikeDbStorageTest {
    private final FilmLikeDbStorage filmLikeDbStorage;

    static Like getTestLike() {
        return Like.builder()
                .filmId(1L)
                .userId(2L)
                .build();
    }

    @Test
    @DisplayName("Add like to film")
    void addLikeFilm() {
        final Like like = getTestLike();
        filmLikeDbStorage.addLikeFilm(like.getFilmId(), like.getUserId());

        assertEquals(2, filmLikeDbStorage.getFilmLikes(like.getFilmId()).size());
    }

    @Test
    @DisplayName("Delete like from film")
    void deleteLikeFilm() {
        filmLikeDbStorage.deleteLikeFilm(1L, 1L);

        assertEquals(0, filmLikeDbStorage.getFilmLikes(1L).size());
    }

    @Test
    @DisplayName("Count likes on the film")
    void countLikeFilm() {
        assertEquals(1, filmLikeDbStorage.getFilmLikes(1).size());
    }
}