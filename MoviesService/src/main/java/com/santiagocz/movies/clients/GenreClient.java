package com.santiagocz.movies.clients;

import com.santiagocz.movies.classes.GenreDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "genres")
public interface GenreClient {

    @GetMapping("/genres/{id}")
    GenreDto getGenreById(@PathVariable Long id);

    @PostMapping("/genres/assign-movie")
    void assignMovieToGenres(@RequestParam Long movieId, @RequestParam List<Long> genresIds);
}

