package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.NonNull;
import ru.yandex.practicum.filmorate.exception.NoSuchUserException;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
public class Film {
    private final int id;
    @NonNull
    private final String name;
    @NonNull
    private final String description;
    @NonNull
    private final LocalDate releaseDate;

    private final int duration;
    private Set<Integer> likes = new HashSet<>();

    private int likesCount = 0;

    public void addLike(int  userId) {
        likes.add(userId);
        likesCount++;
    }

    public void deleteLike(int  userId) throws NoSuchUserException {
        if (likes.contains(userId)) {
            likes.remove(userId);
            likesCount--;
        } else {
            throw new NoSuchUserException("User with id="+ userId+" doesn't add like");
        }

    }
}
