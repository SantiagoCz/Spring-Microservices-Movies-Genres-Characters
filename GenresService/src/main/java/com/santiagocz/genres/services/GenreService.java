package com.santiagocz.genres.services;

import com.santiagocz.genres.entities.Genre;
import com.santiagocz.genres.repositories.GenreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class GenreService {

    @Autowired
    private GenreRepository genreRepository;

    public Genre save(Genre genre){
        return genreRepository.save(genre);
    }

    public void delete(Genre genre){
        genreRepository.delete(genre);
    }

    public List<Genre> findAll(){
        return genreRepository.findAll();
    }

    public Genre getById(Long id){
        return genreRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Genre not found with ID: " + id));
    }
}
