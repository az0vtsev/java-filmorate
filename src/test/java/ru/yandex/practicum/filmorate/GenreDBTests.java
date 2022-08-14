package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.exception.NoSuchGenreException;
import ru.yandex.practicum.filmorate.storage.genre.GenreDBStorage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class GenreDBTests {
    private final GenreDBStorage storage;

    @Test
    void shouldGetGenre() throws NoSuchGenreException {
        assertEquals(6, storage.getGenres().size());
        assertEquals("Комедия", storage.getGenreById(1).getName());
        assertEquals("Боевик", storage.getGenreById(6).getName());
        assertThrows(NoSuchGenreException.class, () -> storage.getGenreById(10));
    }
}
