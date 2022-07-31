package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NoSuchUserException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
@Component("userStorage")
public interface UserStorage {
    User create(User user);
    User update(User updateUser) throws NoSuchUserException;
    User delete(int id) throws NoSuchUserException;
    User getUserById(int id) throws NoSuchUserException;
    List<User> getUsers();
}
