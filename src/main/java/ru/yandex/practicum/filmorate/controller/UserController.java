package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Slf4j
@RestController
public class UserController {
    private int nextId = 0;
    private HashMap<Integer, User> users = new HashMap<>();

    private Integer getNextId() {
        return ++nextId;
    }

    @PostMapping(value = "/users")
    public User create(@RequestBody User user) throws ValidationException {
        log.info("POST /users request received");

        User addUser;
        if (isValid(user)) {
            if (user.getName().isEmpty()) {
                addUser = new User(getNextId(), user.getLogin(),
                        user.getLogin(), user.getEmail(), user.getBirthday());
            } else {
                addUser = new User(getNextId(), user.getName(),
                        user.getLogin(), user.getEmail(), user.getBirthday());
            }
            users.put(addUser.getId(), addUser);
        } else {
            log.error("Request POST /users contains invalid data");
            throw new ValidationException("New user data is not valid");
        }

        log.info("POST /users request done");
        return addUser;
    }

    @PutMapping(value = "/users")
    public User update(@RequestBody User updateUser) throws ValidationException {
        log.info("PUT /users request received");
        Integer updateId = updateUser.getId();
        if (users.containsKey(updateId)) {
            if (isValid(updateUser)) {
                users.put(updateId, updateUser);
            } else {
                log.error("Request PUT /users contains invalid data");
                throw new ValidationException("Update user date is not valid");
            }
        } else {
            log.error("Request PUT /users contains invalid id");
            throw new ValidationException("Update user id is not valid");
        }
        log.info("PUT /users request done");
        return updateUser;
    }

    @GetMapping(value = "/users")
    public List<User> getUsers() {
        log.info("GET /users request received");
        List<User> list = new ArrayList<>(users.values());
        log.info("GET /users request done");
        return list;
    }

    public boolean isValid(User user){
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
