package ru.yandex.practicum.filmorate;


import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.exception.NoSuchFilmException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class FilmValidationTests {
    private FilmController fController;
    private static String name;
    private static String description;
    private static LocalDate date;
    private static int duration;

    @BeforeEach
    public void createController(){
        fController = new FilmController(new FilmService(new InMemoryFilmStorage(), new InMemoryUserStorage()));
    }


    @BeforeAll
    public static void createFields() {
        name = "name";
        description = "description";
        date = LocalDate.of(1999, 10, 5);
        duration = 100;
    }

    @Test
    void ShouldValidateName(){
        String name2 = "";
        Film film1 = new Film(1, name, description, date, duration);
        Film film2= new Film(2, name2, description, date,  duration);
        try {
            fController.create(film1);
        } catch (ValidationException e) {
            System.out.println(e.getMessage());
        }
        assertEquals(film1, fController.getFilms().get(0));
        assertThrows(ValidationException.class, () -> fController.create(film2));
    }

    @Test
    void ShouldValidateDescriptionLength(){
        String line = "qwertyuiopasdfghjklzxcvbnm";
        StringBuilder sb = new StringBuilder();
        for (int i=0; i < 10; i++) {
            sb.append(line);
        }
        String description2 = sb.toString();
        Film film1 = new Film(1, name, description, date, duration);
        Film film2 = new Film(2, name, description2, date, duration);
        try {
            fController.create(film1);
        } catch (ValidationException e) {
            System.out.println(e.getMessage());
        }
        assertEquals(film1, fController.getFilms().get(0));
        assertThrows(ValidationException.class, () -> fController.create(film2));
    }

    @Test
    void ShouldValidateRealiseDate() {
        LocalDate date2 = LocalDate.of(1895, 12, 28);
        LocalDate date3 = LocalDate.of(1700, 10, 5);
        Film film1 = new Film(1, name, description, date, duration);
        Film film2= new Film(2, name, description, date2, duration);
        Film film3= new Film(3, name, description, date3, duration);
        try {
            fController.create(film1);
            fController.create(film2);
        } catch (ValidationException e) {
            System.out.println(e.getMessage());
        }
        assertEquals(film1, fController.getFilms().get(0));
        assertEquals(film2, fController.getFilms().get(1));
        assertThrows(ValidationException.class, () -> fController.create(film3));
    }

    @Test
    void ShouldValidateDuration(){
        Film film1 = new Film(1, name, description, date, 1);
        Film film2= new Film(2, name, description, date, -1);
        Film film3= new Film(3, name, description, date, 0);
        try {
            fController.create(film1);
        } catch (ValidationException e) {
            System.out.println(e.getMessage());
        }
        assertEquals(film1, fController.getFilms().get(0));
        assertThrows(ValidationException.class, () -> fController.create(film2));
        assertThrows(ValidationException.class, () -> fController.create(film3));
    }

    @Test
    void ShouldValidateId(){
        Film film1 = new Film(1, name, description, date, 1);
        Film film2= new Film(2, name, description, date, 2);
        Film film3= new Film(3, name, description, date, 3);
        try {
            fController.create(film1);
            fController.create(film2);
            fController.create(film3);
        } catch (ValidationException e) {
            System.out.println(e.getMessage());
        }
        Film updateFilm1 = new Film(1, name, description, date, 100);
        Film updateFilm2 = new Film(0, name, description, date, 100);
        Film updateFilm3 = new Film(-1, name, description, date, 100);
        try {
            fController.update(updateFilm1);
        } catch (NoSuchFilmException | ValidationException e) {
            System.out.println(e.getMessage());
        }
        assertEquals(updateFilm1, fController.getFilms().get(0));
        assertThrows(NoSuchFilmException.class, () -> fController.update(updateFilm2));
        assertThrows(NoSuchFilmException.class, () -> fController.update(updateFilm3));
    }
}
