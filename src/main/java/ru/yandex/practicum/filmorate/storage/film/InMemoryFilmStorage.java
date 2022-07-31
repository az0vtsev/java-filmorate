package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NoSuchFilmException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Component("inMemoryFilmStorage")
public class InMemoryFilmStorage implements FilmStorage {

    private HashMap<Integer, Film> films = new HashMap<>();
    private int nextId = 0;

    private int getNextId() {
        return ++nextId;
    }

    @Override
    public Film create(Film film) {
        Film addFilm = new Film(getNextId(), film.getName(),
                    film.getDescription(), film.getReleaseDate(), film.getDuration());
        films.put(addFilm.getId(), addFilm);
        return addFilm;
    }

    @Override
    public Film update(Film updateFilm) throws NoSuchFilmException{
        Integer updateId = updateFilm.getId();
        if (films.containsKey(updateId)) {
            films.put(updateId, updateFilm);
        } else {
            throw new NoSuchFilmException("Film with id=" + updateId + " doesn't exist");
        }
        return updateFilm;
    }

    @Override
    public Film delete(int id) throws NoSuchFilmException{
        Film deleteFilm;
        if (films.containsKey(id)) {
            deleteFilm = films.get(id);
            films.remove(id);
        } else {
            throw new NoSuchFilmException("Film with id=" + id + " doesn't exist");
        }
        return deleteFilm;
    }

    @Override
    public Film getFilmById(int id) throws NoSuchFilmException {
        Film film;
        if (films.containsKey(id)) {
            film = films.get(id);
        } else {
            throw new NoSuchFilmException("Film with id=" + id + " doesn't exist");
        }
        return film;
    }

    @Override
    public List<Film> getFilms() {
        return new ArrayList<>(films.values());
    }


}
