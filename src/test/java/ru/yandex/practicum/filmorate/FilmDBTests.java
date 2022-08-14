package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.exception.NoSuchFilmException;
import ru.yandex.practicum.filmorate.exception.NoSuchMpaRatingException;
import ru.yandex.practicum.filmorate.exception.NoSuchUserException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MpaRating;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.film.FilmDBStorage;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class FilmDBTests {
    private final FilmDBStorage storage;
    private final FilmService filmService;
    private final UserService userService;

    @Test
    void shouldCRUDFilm() throws NoSuchMpaRatingException, NoSuchFilmException, NoSuchUserException {
        Set<Genre> genres1 = new HashSet<>();
        Set<Genre> genres2 = new HashSet<>();
        Set<Genre> genres3 = new HashSet<>();
        genres1.add(new Genre(1, "Комедия"));
        genres1.add(new Genre(2, "Драма"));
        genres2.add(new Genre(1, "Комедия"));
        genres3.add(new Genre(1, "Комедия"));
        MpaRating rating = new MpaRating(1, "G");
        User user = new User(1,"login1",
                "name1", "email@email.ru", LocalDate.of(1993,10,10),
                new HashSet<>());
        User user2 = new User(2,"login2",
                "name2", "email@email.ru", LocalDate.of(1993,10,10),
                new HashSet<>());

        Film film1 = new Film(1, "Name1", LocalDate.of(1993,10,10),
                "des1", 100, rating,
                genres1, new HashSet<>());
        Film film2 = new Film(2, "Name2", LocalDate.of(1993,10,10),
                "des2", 100, rating,
                genres2, new HashSet<>());
        Film film3 = new Film(3, "Name3", LocalDate.of(1993,10,10),
                "des3", 100, rating,
                genres3, new HashSet<>());
        Film film1Update = new Film(1, "Name23", LocalDate.of(1993,10,10),
                "des23", 100, rating,
                genres1, new HashSet<>());
        Film film5Update = new Film(5, "Name5", LocalDate.of(1993,10,10),
                "des5", 100, rating,
                genres1, new HashSet<>());

        storage.create(film1);
        storage.create(film2);
        storage.create(film3);

        userService.createUser(user);
        userService.createUser(user2);

        assertEquals(3, storage.getFilms().size());
        assertEquals(film1, storage.getFilmById(1));

        storage.update(film1Update);
        assertEquals(film1Update, storage.getFilmById(1));
        assertThrows(NoSuchFilmException.class, () -> storage.update(film5Update));

        filmService.addLike(3, 1);
        filmService.addLike(3, 2);
        filmService.addLike(1, 1);

        assertEquals(2, storage.getFilmById(3).getLikes().size());
        List<Film> popularFilms = filmService.getPopularFilms(3);
        assertEquals(3, popularFilms.get(0).getId());
        assertEquals(2, popularFilms.get(2).getId());

        storage.delete(3);
        assertEquals(2, storage.getFilms().size());
        assertThrows(NoSuchFilmException.class, ()-> storage.getFilmById(3));
        popularFilms = filmService.getPopularFilms(1);
        assertEquals(1, popularFilms.get(0).getId());
        assertThrows(NoSuchFilmException.class, ()-> storage.delete(5));
    }
}
