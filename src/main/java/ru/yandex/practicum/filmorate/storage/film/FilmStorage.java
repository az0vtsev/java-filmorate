package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NoSuchFilmException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

@Component("filmStorage")
public interface FilmStorage {
    Film create(Film film);
    Film update(Film updateFilm) throws NoSuchFilmException;
    Film delete(int id) throws NoSuchFilmException;
    Film getFilmById(int id) throws NoSuchFilmException;
    List<Film> getFilms();
}


