package com.santiagocz.movies.clients;

import com.santiagocz.movies.classes.CharacterDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "characters")
public interface CharacterClient {

    @GetMapping("/characters/{id}")
    CharacterDto getCharacterById(@PathVariable Long id);

    @PostMapping("/characters/assign-movie")
    void assignMovieToCharacters(@RequestParam Long movieId, @RequestParam List<Long> charactersIds);
}
