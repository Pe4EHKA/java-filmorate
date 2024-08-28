package ru.yandex.practicum.filmorate.repository.film;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.repository.mappers.FilmRowMapper;
import ru.yandex.practicum.filmorate.repository.mappers.GenreRowMapper;

import java.time.LocalDate;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@JdbcTest
@AutoConfigureTestDatabase
@Import({FilmDbStorage.class, FilmRowMapper.class, GenreRowMapper.class})
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@DisplayName("FilmDbStorageTest")
class FilmDbStorageTest {
    private final FilmDbStorage filmDbStorage;

    static Film getTestFilm1() {
        Mpa mpa = Mpa.builder()
                .id(1L)
                .build();
        return Film.builder()
                .id(1L)
                .name("film1")
                .description("desc1")
                .releaseDate(LocalDate.of(1999, 9, 9))
                .duration(10)
                .mpa(mpa)
                .build();
    }

    static Film getTestFilm2() {
        Mpa mpa = Mpa.builder()
                .id(2L)
                .build();
        return Film.builder()
                .id(2L)
                .name("film2")
                .description("desc2")
                .releaseDate(LocalDate.of(1999, 9, 9))
                .duration(10)
                .mpa(mpa)
                .build();
    }

    static Collection<Film> getTestFilms() {
        return List.of(getTestFilm1(), getTestFilm2());
    }


    @Test
    @DisplayName("Getting all films")
    void getAllFilms() {
        Collection<Film> filmsDb = filmDbStorage.getAllFilms();
        assertNotNull(filmsDb);
        assertFalse(filmsDb.isEmpty());
        assertEquals(getTestFilms(), filmDbStorage.getAllFilms());
    }

    @Test
    @DisplayName("Getting film by id")
    void getFilmById() {
        Optional<Film> filmDb = filmDbStorage.getFilmById(1L);

        assertThat(filmDb)
                .isPresent()
                .hasValueSatisfying(film -> assertThat(film).hasFieldOrPropertyWithValue("id", 1L)
                );
        assertEquals(getTestFilm1(), filmDb.get());
    }

    @Test
    @DisplayName("Creating film")
    void createFilm() {
        Film filmToCreate = getTestFilm1();
        filmToCreate.setId(null);
        Film filmDbCreated = filmDbStorage.createFilm(filmToCreate);
        Film filmToCheck = filmDbStorage.getFilmById(filmDbCreated.getId()).get();
        filmToCreate.setId(3L);
        assertEquals(filmToCreate, filmToCheck);
    }

    @Test
    @DisplayName("Updating Film")
    void updateFilm() {
        Film filmToUpdate = getTestFilm1();
        filmToUpdate.setName("updated name");
        filmToUpdate.setDescription("updated description");
        filmToUpdate.setMpa(Mpa.builder().id(4L).build());

        filmDbStorage.updateFilm(filmToUpdate);
        Film updatedFilm = filmDbStorage.getFilmById(filmToUpdate.getId()).get();
        assertEquals(filmToUpdate, updatedFilm);
    }

    @Test
    @DisplayName("Getting all film genres")
    void getFilmGenres() {
        LinkedHashSet<Genre> genresToFilm = new LinkedHashSet<>();
        genresToFilm.add(Genre.builder().id(1L).name("Комедия").build());
        genresToFilm.add(Genre.builder().id(2L).name("Драма").build());

        LinkedHashSet<Genre> genresToFilmDb = filmDbStorage.getFilmGenres(1L);

        assertNotNull(genresToFilmDb);
        assertFalse(genresToFilmDb.isEmpty());
        assertEquals(genresToFilm, genresToFilmDb);
    }

    @Test
    @DisplayName("Adding genres to film")
    void addFilmGenres() {
        LinkedHashSet<Genre> genresToFilm = new LinkedHashSet<>();
        genresToFilm.add(Genre.builder().id(1L).name("Комедия").build());
        genresToFilm.add(Genre.builder().id(2L).name("Драма").build());
        genresToFilm.add(Genre.builder().id(3L).name("Мультфильм").build());

        LinkedHashSet<Genre> genresToInsert = new LinkedHashSet<>();
        genresToInsert.add(Genre.builder().id(3L).name("Мультфильм").build());

        filmDbStorage.addFilmGenres(1L, genresToInsert);

        LinkedHashSet<Genre> genresToFilmDb = filmDbStorage.getFilmGenres(1L);

        assertNotNull(genresToFilmDb);
        assertFalse(genresToFilmDb.isEmpty());
        assertEquals(genresToFilm, genresToFilmDb);
    }
}