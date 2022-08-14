package ru.yandex.practicum.filmorate.storage.mparating;

import ru.yandex.practicum.filmorate.exception.NoSuchMpaRatingException;
import ru.yandex.practicum.filmorate.model.MpaRating;

import java.util.List;


public interface MpaRatingStorage {
    MpaRating create(MpaRating rating);
    MpaRating update(MpaRating updateRating) throws NoSuchMpaRatingException;
    MpaRating delete(int id) throws NoSuchMpaRatingException;
    MpaRating getMpaRatingById(int id) throws NoSuchMpaRatingException;
    List<MpaRating> getMpaRatings();
}
