package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.exception.NoSuchMpaRatingException;
import ru.yandex.practicum.filmorate.storage.mparating.MpaRatingDBStorage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class MpaDBTests {
    private final MpaRatingDBStorage storage;

    @Test
    void shouldGetMpa() throws NoSuchMpaRatingException {
        assertEquals(5, storage.getMpaRatings().size());
        assertEquals("G", storage.getMpaRatingById(1).getName());
        assertEquals("NC-17", storage.getMpaRatingById(5).getName());
        assertThrows(NoSuchMpaRatingException.class, () -> storage.getMpaRatingById(8));
    }
}
