package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.NonNull;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Data
public class Film {
    private final int id;
    @NonNull
    private final String name;
    @NonNull
    private final LocalDate releaseDate;
    @NonNull
    private final String description;
    private final int duration;
    @NonNull
    private final MpaRating mpa;
    private final Set<Genre> genres;
    private final Set<Integer> likes;

    public Map<String, Object> toMap() {
        Map<String, Object> values = new HashMap<>();
        values.put("name", name);
        values.put("description", description);
        values.put("release_date", releaseDate);
        values.put("duration", duration);
        values.put("mpa", mpa.getId());
        return values;
    }
}
