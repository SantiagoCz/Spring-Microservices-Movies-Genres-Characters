package com.santiagocz.movies.controllers;

import com.santiagocz.movies.classes.Validations;
import com.santiagocz.movies.entities.Movie;
import com.santiagocz.movies.services.MovieService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/movies")
public class MovieController {

    @Autowired
    private MovieService movieService;

    @GetMapping("/all")
    public List<Movie> findAll(){
        return movieService.findAll();
    }

    @GetMapping("/{id}")
    public Movie getById(@PathVariable Long id){
        return movieService.getById(id);
    }

    @PostMapping("/save")
    public ResponseEntity<?> save(@RequestBody @Valid Movie request,
                                  BindingResult result){
        ResponseEntity<?> validationResponse = Validations.handleValidationErrors(result);
        if (validationResponse != null) {
            return validationResponse;
        }
        try {
            movieService.save(request);
            return ResponseEntity.ok("Movie has been saved succesfully.");
        } catch (Exception e) {
            String message = "An internal server error occurred: " + e.getMessage();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(message);
        }
    }

    @PutMapping("/edit/{id}")
    public ResponseEntity<?> edit(@RequestBody @Valid Movie request,
                                  @PathVariable Long id,
                                  BindingResult result) {
        ResponseEntity<?> validationResponse = Validations.handleValidationErrors(result);
        if (validationResponse != null) {
            return validationResponse;
        }

        try {
            // Find the current movie by ID
            Movie movie = movieService.getById(id);

            // Update the movie fields
            movie.setTitle(request.getTitle());
            movie.setCreationDate(request.getCreationDate());

            // Save the updated user
            movieService.save(movie);

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
