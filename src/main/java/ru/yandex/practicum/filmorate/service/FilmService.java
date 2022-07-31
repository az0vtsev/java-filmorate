package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NoSuchFilmException;
import ru.yandex.practicum.filmorate.exception.NoSuchUserException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FilmService {
   private final FilmStorage storage;
   private final UserStorage userStorage;

   @Autowired
   public FilmService(@Qualifier("inMemoryFilmStorage") FilmStorage storage,
                      @Qualifier("inMemoryUserStorage") UserStorage userStorage) {
       this.storage = storage;
       this.userStorage = userStorage;
   }

   public Film createFilm(Film film) {
      return storage.create(film);
   }

   public Film updateFilm(Film film) throws NoSuchFilmException {
      return storage.update(film);
   }

   public Film deleteFilm(int filmId) throws NoSuchFilmException {
      return storage.delete(filmId);
   }

   public Film getFilmById(int id) throws NoSuchFilmException {
      return storage.getFilmById(id);
   }

   public List<Film> getFilms() {
       return storage.getFilms();
   }

   public void addLike(int id, int userId) throws NoSuchFilmException, NoSuchUserException {
      userStorage.getUserById(userId);
      Film film = storage.getFilmById(id);
      film.addLike(userId);
   }

   public void deleteLike(int id, int userId) throws NoSuchFilmException, NoSuchUserException {
      userStorage.getUserById(userId);
      Film film = storage.getFilmById(id);
      film.deleteLike(userId);
   }

   public List<Film> getPopularFilms(int count) {
      return storage.getFilms().stream()
              .sorted((film1, film2) -> film2.getLikesCount() - film1.getLikesCount())
              .limit(count)
              .collect(Collectors.toList());
   }

}
