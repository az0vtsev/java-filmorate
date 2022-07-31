package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.NoSuchFilmException;
import ru.yandex.practicum.filmorate.exception.NoSuchUserException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {
    LocalDate VALIDATE_RELEASE_DATE = LocalDate.of(1895, 12, 28);
    int DEFAULT_FILMS_COUNT = 10;
    private final FilmService service;

    @Autowired
    public FilmController(FilmService service){
        this.service = service;
    }

    @GetMapping(value = "/{id}")
    public Film getFilm(@PathVariable int id) throws NoSuchFilmException {
        log.info("GET /films/" + id +" request received");
        Film film = service.getFilmById(id);
        log.info("GET /films/" + id +" request received");
        return film;
    }

    @GetMapping
    public List<Film> getFilms(){
        log.info("GET /films request received");
        List<Film> list = service.getFilms();
        log.info("GET /films request done");
        return list;
    }

    @PostMapping
    public Film create(@RequestBody Film film) throws ValidationException {
        log.info("POST /films request received");
        Film addFilm;
        if (isValid(film)) {
             addFilm = service.createFilm(film);
         } else {
             log.error("Request POST /films contains invalid data");
             throw new ValidationException("New film data is not valid");
         }
        log.info("POST /films request done");
        return addFilm;
    }

    @PutMapping
    public Film update(@RequestBody Film film) throws ValidationException, NoSuchFilmException {
        log.info("PUT /films request received");
        Film updateFilm;
           if (isValid(film)) {
               updateFilm = service.updateFilm(film);
           } else {
               log.error("Request PUT /films contains invalid data");
               throw new ValidationException("Update film data is not valid");
           }
        log.info("PUT /films request done");
        return updateFilm;
    }

    @DeleteMapping(value="/{id}")
    public Film delete(@PathVariable int id) throws NoSuchFilmException {
        log.info("DELETE /films request received");
        Film deleteFilm = service.deleteFilm(id);
        log.info("DELETE /films request done");
        return deleteFilm;
    }

    @PutMapping(value="/{id}/like/{userId}")
    public void addLike(@PathVariable int id, @PathVariable int userId)
            throws NoSuchFilmException, NoSuchUserException {
        log.info("PUT /films/like request received");
        service.addLike(id, userId);
        log.info("PUT /films/like request done");
    }

    @DeleteMapping(value="/{id}/like/{userId}")
    public void deleteLike(@PathVariable int id, @PathVariable int userId)
            throws NoSuchFilmException, NoSuchUserException {
        log.info("DELETE /films/like request received");
        service.deleteLike(id, userId);
        log.info("DELETE /films/like request done");
    }
    @GetMapping(value="/popular")
    public List<Film> getPopularFilms(@RequestParam(required = false) Integer count) {
        return service.getPopularFilms(Objects.requireNonNullElseGet(count, () -> DEFAULT_FILMS_COUNT));
    }

    private boolean isValid(Film film) {
        boolean result = false;
        if (!film.getName().isEmpty() && (film.getDescription().length() <= 200) ) {
            if (film.getReleaseDate().isAfter(VALIDATE_RELEASE_DATE) || film.getReleaseDate().isEqual(VALIDATE_RELEASE_DATE)) {
                if (film.getDuration() > 0) {
                    result = true;
                }
            }
        }
        return result;
    }

}
