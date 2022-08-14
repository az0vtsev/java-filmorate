package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NoSuchUserException;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;


@Component("userDBStorage")
public class UserDBStorage implements UserStorage {
    private final JdbcTemplate jdbcTemplate;

    public UserDBStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public User create(User user) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("users")
                .usingGeneratedKeyColumns("id");
        int id = simpleJdbcInsert.executeAndReturnKey(user.toMap()).intValue();
        return new User(id, user.getLogin(), user.getName(), user.getEmail(), user.getBirthday(),
                new HashSet<>());
    }

    @Override
    public User update(User updateUser) throws NoSuchUserException {
        getUserById(updateUser.getId());
        jdbcTemplate.update(
                "UPDATE users SET login=?, name=?, email=?, birthday=? WHERE id=?",
                updateUser.getLogin(),
                updateUser.getName(),
                updateUser.getEmail(),
                updateUser.getBirthday(),
                updateUser.getId()
        );
        if (updateUser.getFriendsId() != null && !updateUser.getFriendsId().isEmpty()) {
            updateUserFriendsId(updateUser);
        }

        return updateUser;
    }

    @Override
    public User delete(int id) throws NoSuchUserException {
        User user = getUserById(id);
        jdbcTemplate.update(
                "DELETE FROM users WHERE id=?",
                String.valueOf(id)
        );
        return user;
    }

    @Override
    public User getUserById(int id) throws NoSuchUserException {
        User user;
        List<User> results = jdbcTemplate.query(
            "SELECT * FROM users WHERE id=?",
                this::mapRowToUser,
                String.valueOf(id)
        );
        if (results.size() != 0) {
            user = results.get(0);
        } else {
            throw new NoSuchUserException("User with id=" + id + "doesn't exist");
        }
        return user;
    }

    @Override
    public List<User> getUsers() {
        return jdbcTemplate.query(
                "SELECT * FROM users",
                this::mapRowToUser
        );
    }

    private List<Integer> getUserFriendsId(int id) {
        return jdbcTemplate.query(
          "SELECT friend_id FROM friends WHERE user_id=?",
                (ResultSet row, int rowNum) -> row.getInt("friend_id"),
                String.valueOf(id)
        );
    }

    private void updateUserFriendsId(User user) {
        int id = user.getId();
        jdbcTemplate.update(
                "DELETE FROM friends WHERE user_id=?",
                String.valueOf(id)
        );
        List<Integer> userFriendsId = new ArrayList<>(user.getFriendsId());
        for (Integer userFriendId : userFriendsId) {
            jdbcTemplate.update(
                    "INSERT INTO friends (user_id, friend_id) VALUES (?,?)",
                    String.valueOf(id),
                    String.valueOf(userFriendId)
            );
        }
    }

    private User mapRowToUser(ResultSet row, int rowNum) throws SQLException {
        List<Integer> friends = getUserFriendsId(row.getInt("id"));
        return new User(
            row.getInt("id"),
            row.getString("login"),
            row.getString("name"),
            row.getString("email"),
            row.getDate("birthday").toLocalDate(),
            new HashSet<>(friends)
        );
    }
}
