package com.santiagocz.movies.services;

import com.santiagocz.movies.classes.CharacterDto;
import com.santiagocz.movies.classes.GenreDto;
import com.santiagocz.movies.clients.CharacterClient;
import com.santiagocz.movies.clients.GenreClient;
import com.santiagocz.movies.entities.Movie;
import com.santiagocz.movies.repositories.MovieRepository;
import feign.FeignException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@Service
public class MovieService {

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private CharacterClient characterClient;

    @Autowired
    private GenreClient genreClient;

    public Movie save(Movie movie){
        movie.setTitle(movie.getTitle().toUpperCase());
        if (existsByTitle(movie.getTitle())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Movie with this title already exists: " + movie.getTitle());
        }
        return movieRepository.save(movie);
    }

    public Movie update(Movie movie){
        return movieRepository.save(movie);
    }

    public void delete(Movie movie){
        movieRepository.delete(movie);
    }

    public List<Movie> findAll(){
        return movieRepository.findAll();
    }

    public Movie getById(Long id){
        return movieRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "movie not found with ID: " + id));
    }

    public boolean existsByTitle(String title){
        return movieRepository.existsByTitle(title);
    }

    public List<Long> verifyAndAssignCharacters(List<Long> charactersIds) throws Exception {
        List<Long> charactersToAssign = new ArrayList<>();
        for (Long id : charactersIds) {
            try {
                CharacterDto character = characterClient.getCharacterById(id);
                charactersToAssign.add(character.getId());
            } catch (FeignException.NotFound e) {
                throw new Exception("Character with ID " + id + " not found.");
            }
        }
        return charactersToAssign;
    }

    public List<Long> verifyAndAssignGenres(List<Long> genresIds) throws Exception {
        List<Long> genresToAssign = new ArrayList<>();
        for (Long id : genresIds) {
            try {
                GenreDto genre = genreClient.getGenreById(id);
                genresToAssign.add(genre.getId());
            } catch (FeignException.NotFound e) {
                throw new Exception("Genre with ID " + id + " not found.");
            }
        }
        return genresToAssign;
    }

    public void assignMovieToCharacters(Long movieId, List<Long> charactersIds){
        characterClient.assignMovieToCharacters(movieId, charactersIds);
    }

    public void assignMovieToGenres(Long movieId, List<Long> genresIds){
        genreClient.assignMovieToGenres(movieId, genresIds);
    }

}
