package ru.yandex.practicum.filmorate.service;

import lombok.Data;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NoSuchUserException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Data
public class UserService {
    @NonNull
    private final UserStorage storage;
    @Autowired
    public UserService(@Qualifier("userDBStorage") UserStorage storage) {
        this.storage = storage;
    }
    public User getUserById(int id) throws NoSuchUserException {
        return storage.getUserById(id);
    }
    public List<User> getUsers() {
        return storage.getUsers();
    }
    public User createUser(User user) {
        String name;
        if (user.getName().isEmpty()) {
            name = user.getLogin();
        } else {
            name = user.getName();
        }
        User addUser = new User(0, user.getLogin(), name, user.getEmail(),
                user.getBirthday(), new HashSet<>());
        return storage.create(addUser);
    }

    public User updateUser(User user) throws NoSuchUserException {
        return storage.update(user);
    }

    public User deleteUser(int id) throws NoSuchUserException {
        return storage.delete(id);
    }

    public void addFriend(int id, int friendId) throws NoSuchUserException {
        storage.getUserById(friendId);
        User updateUser = getUserById(id);
        if (!updateUser.getFriendsId().contains(friendId)) {
            updateUser.getFriendsId().add(friendId);
            storage.update(updateUser);
        }
    }

    public void deleteFriend(int id, int friendId) throws NoSuchUserException {
        storage.getUserById(friendId);
        User updateUser = getUserById(id);
        if (updateUser.getFriendsId().contains(friendId)) {
            updateUser.getFriendsId().remove(friendId);
            storage.update(updateUser);
        } else {
            throw new NoSuchUserException("Friend with id=" + friendId + "doesn't add");
        }
    }

    public  List<User> getUserFriends(int id) throws NoSuchUserException {
        Set<Integer> userFriendsId = storage.getUserById(id).getFriendsId();
        return getUsersById(userFriendsId);
    }

    public List<User> getUserCommonFriends(int id, int friendId) throws NoSuchUserException {
        Set<Integer> commonFriendsId = new HashSet<>(storage.getUserById(id).getFriendsId());
        commonFriendsId.retainAll(storage.getUserById(friendId).getFriendsId());
        return getUsersById(commonFriendsId);
    }

    private List<User> getUsersById(Set<Integer> userFriendsId) {
        List<User> userFriends = new ArrayList<>();
        userFriendsId.forEach(friendId -> {
            try {
                userFriends.add(storage.getUserById(friendId));
            } catch (NoSuchUserException e) {
                throw new RuntimeException(e);
            }
        });
        return userFriends;
    }
}
