package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class Genre {
    private final int id;
    private final String name;
    public Map<String, Object> toMap() {
        Map<String, Object> values = new HashMap<>();
        values.put("name", name);
        return values;
    }
}
