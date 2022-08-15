package ru.yandex.practicum.filmorate.service;

import lombok.Data;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NoSuchMpaRatingException;
import ru.yandex.practicum.filmorate.model.MpaRating;
import ru.yandex.practicum.filmorate.storage.mparating.MpaRatingStorage;

import java.util.List;

@Service
@Data
public class MpaRatingService {
    @NonNull
    private final MpaRatingStorage storage;

    @Autowired
    public MpaRatingService(@Qualifier("mpaRatingDBStorage") MpaRatingStorage storage) {
        this.storage = storage;
    }
    public MpaRating getMpaRatingById(int id) throws NoSuchMpaRatingException {
        return storage.getMpaRatingById(id);
    }
    public List<MpaRating> getMpaRatings() {
        return storage.getMpaRatings();
    }
}
