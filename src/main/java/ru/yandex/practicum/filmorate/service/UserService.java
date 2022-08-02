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
    public UserService(@Qualifier("inMemoryUserStorage") UserStorage storage) {
        this.storage = storage;
    }

    public User getUserById(int id) throws NoSuchUserException {
        return storage.getUserById(id);
    }

    public User createUser(User user) {
        return storage.create(user);
    }

    public User updateUser(User user) throws NoSuchUserException {
        return storage.update(user);
    }

    public User deleteUser(int id) throws NoSuchUserException {
        return storage.delete(id);
    }

    public void addFriend(int id, int friendId) throws NoSuchUserException {
        User user = storage.getUserById(id);
        User friend = storage.getUserById(friendId);
        user.getFriendsId().add(friendId);
        friend.getFriendsId().add(id);
    }

    public void deleteFriend(int id, int friendId) throws NoSuchUserException {
        User user = storage.getUserById(id);
        User friend = storage.getUserById(friendId);
        user.getFriendsId().remove(friendId);
        friend.getFriendsId().remove(id);
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
