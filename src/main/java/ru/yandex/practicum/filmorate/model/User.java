package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.NonNull;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Data
public class User {
    private final int id;
    @NonNull
    private String login;
    @NonNull
    private String name;
    @NonNull
    private String email;
    @NonNull
    private final LocalDate birthday;
    private  final Set<Integer> friendsId;

    public Map<String, Object> toMap() {
        Map<String, Object> values = new HashMap<>();
        values.put("login", login);
        values.put("name", name);
        values.put("email", email);
        values.put("birthday", birthday);
        return values;
    }



}
