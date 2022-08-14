package ru.yandex.practicum.filmorate.storage.genre;


import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NoSuchGenreException;
import ru.yandex.practicum.filmorate.model.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component("genreDBStorage")
public class GenreDBStorage implements GenreStorage {
    private final JdbcTemplate jdbcTemplate;
    public GenreDBStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Genre create(Genre genre) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("genres")
                .usingGeneratedKeyColumns("id");
        int id = simpleJdbcInsert.executeAndReturnKey(genre.toMap()).intValue();
        return new Genre(id, genre.getName());
    }

    @Override
    public Genre update(Genre updateGenre) throws NoSuchGenreException {
        int id = updateGenre.getId();
        getGenreById(id);
        jdbcTemplate.update(
                "UPDATE genres SET name=? WHERE id=?",
                updateGenre.getName(),
                id
        );
        return updateGenre;
    }

    @Override
    public Genre delete(int id) throws NoSuchGenreException {
        Genre genre = getGenreById(id);
        jdbcTemplate.update(
                "DELETE FROM genres WHERE id=?",
                id
        );
        return genre;
    }

    @Override
    public Genre getGenreById(int id) throws NoSuchGenreException {
        Genre genre;
        List<Genre> results = jdbcTemplate.query(
                "SELECT * FROM genres WHERE id=?",
                this::mapRowToGenre,
                String.valueOf(id)
        );
        if (results.size() != 0) {
            genre = results.get(0);
        } else {
            throw new NoSuchGenreException("Genre with id=" + id + "doesn't exist");
        }
        return genre;
    }

    @Override
    public List<Genre> getGenres() {
        return jdbcTemplate.query(
                "SELECT * FROM genres",
                this::mapRowToGenre
        );
    }

    private Genre mapRowToGenre(ResultSet row, int rowNum) throws SQLException {
        return new Genre(
                row.getInt("id"),
                row.getString("name")
                );
    }
}
