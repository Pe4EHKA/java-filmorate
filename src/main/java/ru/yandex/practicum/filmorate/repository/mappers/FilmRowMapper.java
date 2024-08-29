package ru.yandex.practicum.filmorate.repository.mappers;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;

@Component
public class FilmRowMapper implements RowMapper<Film> {
    private final Map<Long, Film> filmMap = new LinkedHashMap<>();

    @Override
    public Film mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        long filmId = resultSet.getLong("film_id");

        Film film = filmMap.get(filmId);
        if (film == null) {
            String name = resultSet.getString("film_name");
            String description = resultSet.getString("film_description");
            LocalDate releaseDate = resultSet.getDate("film_release_date").toLocalDate();
            int duration = resultSet.getInt("film_duration");

            Mpa mpa = Mpa.builder()
                    .id(resultSet.getLong("mpa_id"))
                    .name(resultSet.getString("mpa_name"))
                    .build();

            film = Film.builder()
                    .id(filmId)
                    .name(name)
                    .description(description)
                    .releaseDate(releaseDate)
                    .duration(duration)
                    .mpa(mpa)
                    .genres(new LinkedHashSet<>())
                    .build();

            filmMap.put(filmId, film);
        }

        long genreId = resultSet.getLong("genre_id");
        if (!resultSet.wasNull()) {
            String genreName = resultSet.getString("genre_name");
            Genre genre = Genre.builder()
                    .id(genreId)
                    .name(genreName)
                    .build();
            film.getGenres().add(genre);
        }
        return film;
    }

    public Optional<Film> getFilm() {
        return filmMap.values().stream().findFirst();
    }

    public Collection<Film> getAllFilms() {
        return List.copyOf(filmMap.values());
    }
}
