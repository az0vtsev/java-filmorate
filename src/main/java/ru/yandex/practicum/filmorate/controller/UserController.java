package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.NoSuchUserException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService service;
    @Autowired
    public UserController(UserService service){
        this.service = service;
    }

    @GetMapping(value = "/{id}")
    public User getUser(@PathVariable int id) throws NoSuchUserException {
        log.info("GET /users/" + id +" request received");
        User user = service.getUserById(id);
        log.info("GET /users/" + id +" request received");
        return user;
    }

    @GetMapping
    public List<User> getUsers() {
        log.info("GET /users request received");
        List<User> list = service.getUsers();
        log.info("GET /users request done");
        return list;
    }

    @PostMapping
    public User create(@RequestBody User user) throws ValidationException{
        log.info("POST /users request received");
        User addUser;
        if (isValid(user)) {
            addUser = service.createUser(user);
        } else {
            log.error("Request POST /users contains invalid data");
            throw new ValidationException("New user data is not valid");
        }
        log.info("POST /users request done");
        return addUser;
    }

    @PutMapping
    public User update(@RequestBody User user) throws ValidationException, NoSuchUserException {
        log.info("PUT /users request received");
        User updateUser;
            if (isValid(user)) {
                updateUser = service.updateUser(user);
            } else {
                log.error("Request PUT /users contains invalid data");
                throw new ValidationException("Update user data is not valid");
            }
        log.info("PUT /users request done");
        return updateUser;
    }

    @DeleteMapping(value="/{id}")
    public User delete(@PathVariable int id) throws NoSuchUserException {
        log.info("DELETE /users request received");
        User deleteUser = service.deleteUser(id);
        log.info("DELETE /users request done");
        return deleteUser;
    }

    @PutMapping(value="/{id}/friends/{friendId}")
    public void addFriend(@PathVariable int id, @PathVariable int friendId) throws NoSuchUserException {
        log.info("PUT /friends request received");
        service.addFriend(id, friendId);
        log.info("PUT /friends request done");
    }

    @DeleteMapping(value ="/{id}/friends/{friendId}")
    public void deleteFriend(@PathVariable int id, @PathVariable int friendId) throws NoSuchUserException {
        log.info("DELETE /friends request received");
        service.deleteFriend(id, friendId);
        log.info("DELETE /friends request done");
    }

    @GetMapping(value="/{id}/friends")
    public List<User> getUserFriends(@PathVariable int id) throws NoSuchUserException {
        log.info("GET users/friends request received");
        List<User> userFriends = service.getUserFriends(id);
        log.info("GET users/friends request done");
        return userFriends;
    }

    @GetMapping(value="/{id}/friends/common/{otherId}")
    public List<User> getUserCommonFriends(@PathVariable int id, @PathVariable int otherId)
            throws NoSuchUserException {
        log.info("GET users/friends/common request received");
        List<User> userCommonFriends = service.getUserCommonFriends(id, otherId);
        log.info("GET users/friends/common request done");
        return userCommonFriends;
    }

    private boolean isValid(User user){
        LocalDate validBirthday = LocalDate.now();
        boolean result = false;
        if (!user.getEmail().isEmpty() && user.getEmail().contains("@")) {
            if (!user.getLogin().isEmpty() && !user.getLogin().contains(" ")) {
                if (user.getBirthday().isBefore(validBirthday)) {
                    result = true;
                }
            }
        }
        return result;
    }

}
