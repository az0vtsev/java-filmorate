package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.exception.NoSuchGenreException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.service.GenreService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/genres")
public class GenreController {
    private final GenreService service;
    @Autowired
    public GenreController(GenreService service) {
        this.service = service;
    }

    @GetMapping(value = "/{id}")
    public Genre getGenre(@PathVariable int id) throws NoSuchGenreException {
        log.info("GET /genres/" + id +" request received");
        Genre genre = service.getGenreById(id);
        log.info("GET /genres/" + id +" request received");
        return genre;
    }

    @GetMapping
    public List<Genre> getGenres(){
        log.info("GET /genres request received");
        List<Genre> list = service.getGenres();
        log.info("GET /genres request done");
        return list;
    }
}
