package com.santiagocz.movies.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.santiagocz.movies.classes.Validations;
import com.santiagocz.movies.entities.Movie;
import com.santiagocz.movies.services.ImageService;
import com.santiagocz.movies.services.MovieService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/movies")
public class MovieController {

    @Autowired
    private MovieService movieService;

    @Autowired
    private ImageService imageService;

    @Autowired
    private ObjectMapper objectMapper;

    @GetMapping("/all")
    public List<Movie> findAll(){
        return movieService.findAll();
    }

    @GetMapping("/{id}")
    public Movie getById(@PathVariable Long id){
        return movieService.getById(id);
    }

    @Transactional
    @PostMapping("/save")
    public ResponseEntity<?> save(@RequestParam(value = "image", required = false) MultipartFile image,
                                  @RequestParam("movie") String movieJson) {
        try {
            // Convert the received JSON to a Movie object
            Movie request = objectMapper.readValue(movieJson, Movie.class);

            ResponseEntity<?> validationResponse = Validations.validateFields(request);
            if (validationResponse != null) {
                return validationResponse;
            }

            // Check the existence of each character and assign their IDs to the movie
            List<Long> charactersToAssign = movieService.verifyAndAssignCharacters(request.getCharactersIds());
            request.setCharactersIds(charactersToAssign);

            // Check the existence of each genre and assign their IDs to the movie
            List<Long> genresToAssign = movieService.verifyAndAssignGenres(request.getGenresIds());
            request.setGenresIds(genresToAssign);

            movieService.save(request);

            if (image != null && !image.isEmpty()) {
                imageService.saveImage(image, request);
            }

            // Assign the movie to the characters
            movieService.assignMovieToCharacters(request.getId(), request.getCharactersIds());
            // Assing the movie to the genres
            movieService.assignMovieToGenres(request.getId(), request.getGenresIds());


            return ResponseEntity.ok("Movie has been saved successfully.");
        } catch (Exception e) {
            String message = "An internal server error occurred: " + e.getMessage();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(message);
        }
    }

    @Transactional
    @PutMapping("/update/{id}")
    public ResponseEntity<?> update(@PathVariable Long id,
                                    @RequestParam(value = "image", required = false) MultipartFile image,
                                    @RequestParam("movie") String movieJson) {
        try {
            // Find the current movie by ID
            Movie movie = movieService.getById(id);

            // Convert the received JSON to a Movie object
            Movie request = objectMapper.readValue(movieJson, Movie.class);

            ResponseEntity<?> validationResponse = Validations.validateFields(request);
            if (validationResponse != null) {
                return validationResponse;
            }

            // Check if the title has changed
            if (!movie.getTitle().equalsIgnoreCase(request.getTitle())) {
                // If the name has changed, check if the new name already exists
                if (movieService.existsByTitle(request.getTitle())) {
                    throw new ResponseStatusException(HttpStatus.CONFLICT, "Movie with this title already exists: " + request.getTitle());
                }
            }

            // Update the movie fields
            movie.setTitle(request.getTitle().toUpperCase());
            movie.setCreationDate(request.getCreationDate());
            movie.setRating(request.getRating());

            movieService.update(movie);

            if (image != null && !image.isEmpty()) {
                imageService.saveImage(image, movie);
            }

            return ResponseEntity.ok("Movie updated successfully");
        } catch (Exception e) {
            String message = "An internal server error occurred: " + e.getMessage();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(message);
        }
    }

    @Transactional
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id){
        try {
            Movie movie = movieService.getById(id);
            movieService.delete(movie);

            return ResponseEntity.ok("Movie has been deleted successfully");

        } catch (Exception e) {
            String message = "An internal server error occurred: " + e.getMessage();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(message);
        }
    }
}
