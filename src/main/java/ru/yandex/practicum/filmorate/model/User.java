package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.NonNull;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
public class User {

    private final int id;
    @NonNull
    private String name;
    @NonNull
    private String login;
    @NonNull
    private String email;
    @NonNull
    private final LocalDate birthday;

    private final Set<Integer> friendsId = new HashSet<>();



}
