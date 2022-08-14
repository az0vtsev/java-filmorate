package ru.yandex.practicum.filmorate.service;

import lombok.Data;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NoSuchGenreException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.genre.GenreStorage;

import java.util.List;

@Service
@Data
public class GenreService {
    @NonNull
    private final GenreStorage storage;

    @Autowired
    public GenreService(@Qualifier("genreDBStorage") GenreStorage storage) {
        this.storage = storage;
    }

    public Genre getGenreById(int id) throws NoSuchGenreException {
        return storage.getGenreById(id);
    }
    public List<Genre> getGenres() {
        return storage.getGenres();
    }

}
