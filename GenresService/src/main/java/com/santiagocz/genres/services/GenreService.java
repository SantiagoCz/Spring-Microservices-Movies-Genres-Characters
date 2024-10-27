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

    public Genre save(Genre genre) {
        genre.setName(genre.getName().toUpperCase());
        if (existsByName(genre.getName())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Genre with this name already exists: " + genre.getName());
        }
        return genreRepository.save(genre);
    }

    public Genre update(Genre genre) {
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

    public boolean existsByName(String name) {
        return genreRepository.existsByName(name);
    }

    // Method to assign a movie to multiple characters
    public void assignMovieToGenres(Long movieId, List<Long> genresIds) {
        // Iterate over the IDs of the genres to assign them to the movie
        for (Long genreId : genresIds) {
            Genre genre = getById(genreId);

            // Verify if the genre is already assigned to the movie
            if (!genre.getMoviesIds().contains(movieId)) {
                genre.getMoviesIds().add(movieId);
                genreRepository.save(genre);
            }
        }
    }
}
