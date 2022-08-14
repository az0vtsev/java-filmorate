package ru.yandex.practicum.filmorate.storage.mparating;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NoSuchMpaRatingException;
import ru.yandex.practicum.filmorate.model.MpaRating;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component("mpaRatingDBStorage")
public class MpaRatingDBStorage implements MpaRatingStorage {
    private final JdbcTemplate jdbcTemplate;
    public MpaRatingDBStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public MpaRating create(MpaRating rating) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("mpa_rating")
                .usingGeneratedKeyColumns("id");
        int id = simpleJdbcInsert.executeAndReturnKey(rating.toMap()).intValue();
        return new MpaRating(id, rating.getName());
    }

    @Override
    public MpaRating update(MpaRating updateRating) throws NoSuchMpaRatingException {
        int id = updateRating.getId();
        getMpaRatingById(id);
        jdbcTemplate.update(
                "UPDATE mpa_rating SET name=? WHERE id=?",
                updateRating.getName(),
                id
        );
        return updateRating;
    }

    @Override
    public MpaRating delete(int id) throws NoSuchMpaRatingException {
        MpaRating rating = getMpaRatingById(id);
        jdbcTemplate.update(
                "DELETE FROM mpa_rating WHERE id=?",
                id
        );
        return rating;
    }

    @Override
    public MpaRating getMpaRatingById(int id) throws NoSuchMpaRatingException {
        MpaRating rating;
        List<MpaRating> results = jdbcTemplate.query(
                "SELECT * FROM mpa_rating WHERE id=?",
                this::mapRowToMpaRating,
                String.valueOf(id)
        );
        if (results.size() != 0) {
            rating = results.get(0);
        } else {
            throw new NoSuchMpaRatingException("MPA rating with id=" + id + "doesn't exist");
        }
        return rating;
    }

    @Override
    public List<MpaRating> getMpaRatings() {
        return jdbcTemplate.query(
                "SELECT * FROM mpa_rating",
                this::mapRowToMpaRating
        );
    }

    private MpaRating mapRowToMpaRating(ResultSet row, int rowNum) throws SQLException {
        MpaRating rating = new MpaRating(row.getInt("id"),
                row.getString("name"));
        return rating;
    }
}
