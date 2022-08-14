package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.exception.NoSuchMpaRatingException;
import ru.yandex.practicum.filmorate.model.MpaRating;
import ru.yandex.practicum.filmorate.service.MpaRatingService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/mpa")
public class MpaRatingController {
    private final MpaRatingService service;
    @Autowired
    public MpaRatingController(MpaRatingService service) {
        this.service = service;
    }

    @GetMapping(value = "/{id}")
    public MpaRating getMpaRating(@PathVariable int id) throws NoSuchMpaRatingException {
        log.info("GET /mpa/" + id +" request received");
        MpaRating rating = service.getMpaRatingById(id);
        log.info("GET /mpa/" + id +" request received");
        return rating;
    }

    @GetMapping
    public List<MpaRating> getMpaRatings(){
        log.info("GET /mpa request received");
        List<MpaRating> list = service.getMpaRatings();
        log.info("GET /mpa request done");
        return list;
    }
}
