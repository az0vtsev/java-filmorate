package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NoSuchFilmException;
import ru.yandex.practicum.filmorate.exception.NoSuchMpaRatingException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MpaRating;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component("filmDBStorage")
public class FilmDBStorage implements FilmStorage {
    private final JdbcTemplate jdbcTemplate;

    public FilmDBStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Film create(Film film) throws NoSuchMpaRatingException {

        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("films")
                .usingGeneratedKeyColumns("id");
        int id = simpleJdbcInsert.executeAndReturnKey(film.toMap()).intValue();

        Set<Genre> genres;
        if (film.getGenres() != null && !film.getGenres().isEmpty()) {
            addFilmGenres(id, new ArrayList<>(film.getGenres()));
            genres = film.getGenres();
        } else {
            genres = new HashSet<>();
        }
        return new Film(id, film.getName(), film.getReleaseDate(), film.getDescription(),
                film.getDuration(), film.getMpa(), genres, new HashSet<>());
    }

    @Override
    public Film update(Film updateFilm) throws NoSuchFilmException{
        getFilmById(updateFilm.getId());
        jdbcTemplate.update(
                "UPDATE films SET name=?, description=?, release_date=?, duration=?, " +
                        "mpa=? WHERE id=?",
                updateFilm.getName(),
                updateFilm.getDescription(),
                updateFilm.getReleaseDate(),
                updateFilm.getDuration(),
                updateFilm.getMpa().getId(),
                updateFilm.getId()
        );
        Set<Genre> updateGenres;
        if (updateFilm.getGenres() != null && !updateFilm.getGenres().isEmpty()) {
            updateFilmGenres(updateFilm.getId(), new ArrayList<>(updateFilm.getGenres()));
            updateGenres = updateFilm.getGenres().stream()
                    .sorted(Comparator.comparingInt(Genre::getId))
                    .collect(Collectors.toCollection(LinkedHashSet::new));
        } else {
            deleteFilmGenres(updateFilm.getId());
            updateGenres = new HashSet<>();
        }
        if (updateFilm.getLikes() != null && !updateFilm.getLikes().isEmpty() ) {
            updateFilmLikes(updateFilm.getId(), new ArrayList<>(updateFilm.getLikes()));
        }

        return new Film(updateFilm.getId(), updateFilm.getName(), updateFilm.getReleaseDate(),
                updateFilm.getDescription(), updateFilm.getDuration(),updateFilm.getMpa(),
                updateGenres, updateFilm.getLikes());
    }

    @Override
    public Film delete(int id) throws NoSuchFilmException {
        Film film = getFilmById(id);
        jdbcTemplate.update(
                "DELETE FROM films WHERE id=?",
                String.valueOf(id)
        );
        return film;
    }

    @Override
    public Film getFilmById(int id) throws NoSuchFilmException {
        Film film;
        List<Film> results = jdbcTemplate.query(
                "SELECT * FROM films WHERE id=?",
                this::mapRowToFilm,
                id
        );
        if (results.size() != 0) {
            film = results.get(0);
        } else {
            throw new NoSuchFilmException("Film with id=" + id + "doesn't exist");
        }
        return film;
    }

    @Override
    public List<Film> getFilms() {
        return jdbcTemplate.query(
                "SELECT * FROM films",
                this::mapRowToFilm
        );
    }

    private Film mapRowToFilm(ResultSet row, int rowNum) throws SQLException {
        int id = row.getInt("id");
        List<Genre> genres = getFilmGenres(id);
        List<Integer> userLikesId = getUserLikesId(id);
        int mpaId = row.getInt("mpa");
        String mpaName = getMpaName(mpaId);
        Set<Genre> genresSet = new TreeSet<>(Comparator.comparingInt(Genre::getId));
        genresSet.addAll(genres);
        return new Film(
                id,
                row.getString("name"),
                row.getDate("release_date").toLocalDate(),
                row.getString("description"),
                row.getInt("duration"),
                new MpaRating(mpaId,
                        mpaName),
                genresSet,
                new HashSet<>(userLikesId)
        );
    }
    private String getMpaName(int id) {
        return jdbcTemplate.queryForObject(
                "SELECT name FROM mpa_rating WHERE id=?",
                (ResultSet row, int rowNum) -> row.getString("name"),
                String.valueOf(id)
        );
    }

    private List<Integer> getUserLikesId(int filmId){
        return  jdbcTemplate.query(
                "SELECT user_id FROM likes WHERE film_id=?",
                (ResultSet row, int rowNum) -> row.getInt("user_id"),
                String.valueOf(filmId)
        );
    }

    private List<Integer> getFilmGenresId(int filmId){
        return  jdbcTemplate.query(
                "SELECT genre_id FROM films_genres WHERE film_id=?",
                (ResultSet row, int rowNum) -> row.getInt("genre_id"),
                String.valueOf(filmId)
        );
    }

    private List<Genre> getFilmGenres(int filmId) {
        List<Integer> genresId = getFilmGenresId(filmId);
        List<Genre> genres = new ArrayList<>();
        for (Integer genreId : genresId) {
            genres.add( jdbcTemplate.queryForObject(
                    "SELECT * FROM genres WHERE id=? ORDER BY id",
                    (ResultSet row, int rowNum) -> new Genre(genreId, row.getString("name")),
                    String.valueOf(genreId)
                    )
            );
        }
        return genres;
    }

    private void updateFilmGenres(int filmId, List<Genre> filmGenres) {
        jdbcTemplate.update(
                "DELETE FROM films_genres WHERE film_id=?",
                String.valueOf(filmId)
        );
        addFilmGenres(filmId, filmGenres);
    }

    private void updateFilmLikes (int filmId, List<Integer> filmLikes) {
        jdbcTemplate.update(
                "DELETE FROM likes WHERE film_id=?",
                filmId
        );
        addFilmLikes(filmId, filmLikes);
    }

    private void addFilmLikes(int filmId, List<Integer> filmLikes) {
        for (Integer like : filmLikes) {
            jdbcTemplate.update(
                    "INSERT INTO likes (user_id, film_id) VALUES (?,?)",
                    like,
                    filmId
            );
        }
    }

    private void addFilmGenres(int filmId, List<Genre> filmGenres) {

        for (Genre filmGenre : filmGenres) {
            jdbcTemplate.update(
                    "INSERT INTO films_genres (film_id, genre_id) VALUES (?,?)",
                    String.valueOf(filmId),
                    String.valueOf(filmGenre.getId())
            );
        }
    }

    private void deleteFilmGenres(int filmId) throws NoSuchFilmException {
        jdbcTemplate.update(
                "DELETE FROM films_genres WHERE film_id=?",
                filmId
        );
    }
}
