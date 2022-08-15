package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.exception.NoSuchUserException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.user.UserDBStorage;

import java.time.LocalDate;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@AutoConfigureTestDatabase
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserDBTests {
    private final UserDBStorage  storage;
    private final UserService service;

    @Test
    void ShouldCRUDUser() throws NoSuchUserException {

        User user = new User(1,"login",
                "name", "email@email.ru", LocalDate.of(1993,10,10),
                new HashSet<>());
        User user2 = new User(2,"login2",
                "name2", "email2@email.ru", LocalDate.of(1993,11,10),
                new HashSet<>());
        User userUpdate = new User(1,"loginUpdate",
                "nameUpdate", "email@email.ru", LocalDate.of(1993,10,10),
                new HashSet<>());
        User userUpdate2 = new User(5,"loginUpdate5",
                "nameUpdate5", "email@email.ru", LocalDate.of(1993,10,10),
                new HashSet<>());
        User user3 = new User(3,"login3",
                "name3", "email2@email.ru", LocalDate.of(1993,11,10),
                new HashSet<>());

        storage.create(user);
        storage.create(user2);
        storage.create(user3);

        assertEquals(user, storage.getUserById(1));
        assertEquals(user2, storage.getUserById(2));

        storage.update(userUpdate);
        assertEquals(userUpdate, storage.getUserById(1));
        assertThrows(NoSuchUserException.class, () -> storage.update(userUpdate2));

        service.addFriend(1,2);
        service.addFriend(1,3);
        service.addFriend(3,2);


        assertEquals(2, service.getUserFriends(1).size());
        assertEquals(0, service.getUserFriends(2).size());

        assertThrows(NoSuchUserException.class, () -> service.addFriend(1,5));
        assertThrows(NoSuchUserException.class, () -> service.addFriend(5,1));

        assertEquals(user2, service.getUserFriends(3).get(0));
        assertEquals(1, service.getUserCommonFriends(1,3).size());
        assertEquals(user2, service.getUserCommonFriends(1,3).get(0));

        service.deleteFriend(1,2);
        assertEquals(1, service.getUserFriends(1).size());
        assertEquals(3, service.getUserFriends(1).get(0).getId());
        assertEquals(3, storage.getUsers().size());

        assertThrows(NoSuchUserException.class, () -> service.deleteFriend(1,5));
        assertThrows(NoSuchUserException.class, () -> service.deleteFriend(5,1));



        storage.delete(1);
        assertEquals(2, storage.getUsers().size());
        assertEquals(user2, storage.getUsers().get(0));

        assertThrows(NoSuchUserException.class, () -> storage.delete(5));
    }
}
