package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class UserValidationTests {

    private UserController uController;
    static private String name;
    static private String login = "login";
    static private String email = "test@yandex.ru";
    static private LocalDate date = LocalDate.of(1999, 10, 5);

    @BeforeAll
    public static void createFields(){
        name = "name";
        login = "login";
        email = "test@yandex.ru";
        date = LocalDate.of(1999, 10, 5);
    }

    @BeforeEach
    public void createController() {
        uController = new UserController();
    }

    @Test
    void ShouldValidateUserEmail(){
        String email2 = " ";
        String email3 = "testyandex.ru";
        User user1 = new User(1, name, login, email, date);
        User user2 = new User(2, name, login, email2, date);
        User user3 = new User(3, name, login, email3, date);
        try {
            uController.create(user1);
        } catch (ValidationException e) {
            System.out.println(e.getMessage());
        }
        assertEquals(user1, uController.getUsers().get(0));
        assertThrows(ValidationException.class, () -> uController.create(user2));
        assertThrows(ValidationException.class, () -> uController.create(user3));
    }

    @Test
    void ShouldValidateLogin() {
        String login2 = "";
        String login3 = "lo gin";
        User user1 = new User(1, name, login, email, date);
        User user2 = new User(2, name, login2, email, date);
        User user3 = new User(3, name, login3, email, date);
        try {
            uController.create(user1);
        } catch (ValidationException e) {
            System.out.println(e.getMessage());
        }
        assertEquals(user1, uController.getUsers().get(0));
        assertThrows(ValidationException.class, () -> uController.create(user2));
        assertThrows(ValidationException.class, () -> uController.create(user3));
    }

    @Test
    void ShouldUseLoginAsNameIfNameIsEmpty() {
        User user1 = new User(1, "",login, email, date);
        try {
            uController.create(user1);
        } catch (ValidationException e) {
            System.out.println(e.getMessage());
        }
        assertEquals(user1.getLogin(), uController.getUsers().get(0).getName());
    }

    @Test
    void ShouldValidateBirthday(){
        LocalDate date2 = LocalDate.of(2023, 10, 5);
        User user1 = new User(1, name, login, email, date);
        User user2 = new User(2, name,login, email, date2);
        try {
            uController.create(user1);
        } catch (ValidationException e) {
            System.out.println(e.getMessage());
        }
        assertEquals(user1, uController.getUsers().get(0));
        assertThrows(ValidationException.class, () -> uController.create(user2));
    }

    @Test
    void ShouldValidateId(){
        User user1 = new User(1, name, login, email, date);
        User user2 = new User(2, name, login, email, date);
        User user3 = new User(3, name, login, email, date);
        try {
            uController.create(user1);
            uController.create(user2);
            uController.create(user3);
        } catch (ValidationException e) {
            System.out.println(e.getMessage());
        }
        User updateUser1 = new User(1, "update name", login, email, date);
        User updateUser2 = new User(0, "update name", login, email, date);
        User updateUser3 = new User(-1,"update name", login, email, date);
        try {
            uController.update(updateUser1);
        } catch (ValidationException e) {
            System.out.println(e.getMessage());
        }
        assertEquals(updateUser1, uController.getUsers().get(0));
        assertThrows(ValidationException.class, () -> uController.update(updateUser2));
        assertThrows(ValidationException.class, () -> uController.update(updateUser3));
    }
}
