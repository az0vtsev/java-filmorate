package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NoSuchUserException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Component("inMemoryUserStorage")
public class InMemoryUserStorage implements UserStorage {
    private int nextId = 0;
    private HashMap<Integer, User> users = new HashMap<>();

    private Integer getNextId() {
        return ++nextId;
    }

    @Override
    public User create(User user){
        User addUser;

            if (user.getName().isEmpty()) {
                addUser = new User(getNextId(), user.getLogin(),
                        user.getLogin(), user.getEmail(), user.getBirthday());
            } else {
                addUser = new User(getNextId(), user.getName(),
                        user.getLogin(), user.getEmail(), user.getBirthday());
            }
            users.put(addUser.getId(), addUser);

        return addUser;
    }

    @Override
    public User update(User updateUser) throws NoSuchUserException {
        Integer updateId = updateUser.getId();
        if (users.containsKey(updateId)) {
            users.put(updateId, updateUser);
        } else {
            throw new NoSuchUserException("User with id=" + updateId + " doesn't exist");
        }

        return updateUser;
    }

    @Override
    public User delete(int id) throws NoSuchUserException {
        User deleteUser;
        if (users.containsKey(id)) {
            deleteUser = users.get(id);
            users.remove(id);
        } else {
            throw new NoSuchUserException("User with id=" + id + " doesn't exist");
        }
        return deleteUser;
    }

    @Override
    public User getUserById(int id) throws NoSuchUserException {
        User user;
        if (users.containsKey(id)) {
            user = users.get(id);
        } else {
            throw new NoSuchUserException("User with id=" + id + " doesn't exist");
        }
        return user;
    }

    @Override
    public List<User> getUsers() {
        return new ArrayList<>(users.values());
    }


}
