package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.annotation.Marker;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.Collection;

@RestController
@RequestMapping("/films")
@RequiredArgsConstructor
@Slf4j
public class FilmController {

    private final FilmService filmService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Collection<Film> getAllFilms() {
        log.info("Request for all films");
        return filmService.getAllFilms();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Film getFilmById(@PathVariable(name = "id") int filmId) {
        log.info("Request for a film with id {}", filmId);
        return filmService.getFilmById(filmId);
    }

    @GetMapping("/popular")
    @ResponseStatus(HttpStatus.OK)
    public Collection<Film> getPopularFilms(@RequestParam(name = "count", defaultValue = "10") int count) {
        log.info("Request for popular films");
        if (count <= 0) {
            throw new ValidationException("count must be greater than 0");
        }
        return filmService.getPopularFilms(count);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Validated({Marker.OnCreate.class})
    public Film createFilm(@Valid @RequestBody Film film) {
        log.info("Request to create film: {}", film);
        if (film == null) {
            throw new ValidationException("Film is null");
        }
        return filmService.createFilm(film);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    @Validated({Marker.OnUpdate.class})
    public Film updateFilm(@Valid @RequestBody Film film) {
        log.info("Request to update film: {}", film);
        if (film == null) {
            throw new ValidationException("Film is null");
        }
        return filmService.updateFilm(film);
    }

    @PutMapping("/{id}/like/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public void addLike(@PathVariable(name = "id") long filmId,
                        @PathVariable(name = "userId") long userId) {
        log.info("Request to add like to film with id {} from user {}", filmId, userId);
        filmService.addLike(filmId, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public void removeLike(@PathVariable(name = "id") long filmId,
                           @PathVariable(name = "userId") long userId) {
        log.info("Request to remove like from film with id {} from user {}", filmId, userId);
        filmService.removeLike(filmId, userId);
    }
}
