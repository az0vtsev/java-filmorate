package ru.yandex.practicum.filmorate.storage.genre;

import ru.yandex.practicum.filmorate.exception.NoSuchGenreException;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;

public interface GenreStorage {
    Genre create(Genre genre);
    Genre update(Genre genre) throws NoSuchGenreException;
    Genre delete(int id) throws NoSuchGenreException;
    Genre getGenreById(int id) throws NoSuchGenreException;
    List<Genre> getGenres();
}
