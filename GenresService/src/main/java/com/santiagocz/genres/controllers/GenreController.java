package com.santiagocz.genres.controllers;

import com.santiagocz.genres.classes.Validations;
import com.santiagocz.genres.entities.Genre;
import com.santiagocz.genres.services.GenreService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/genres")
public class GenreController {

    @Autowired
    private GenreService genreService;

    @GetMapping("/all")
    public List<Genre> findAll(){
        return genreService.findAll();
    }

    @GetMapping("/{id}")
    public Genre getById(@PathVariable Long id){
        return genreService.getById(id);
    }

    @PostMapping("/save")
    public ResponseEntity<?> save(@RequestBody @Valid Genre request,
                                  BindingResult result){
        ResponseEntity<?> validationResponse = Validations.handleValidationErrors(result);
        if (validationResponse != null) {
            return validationResponse;
        }
        try {
            genreService.save(request);
            return ResponseEntity.ok("Genre has been saved succesfully.");
        } catch (Exception e) {
            String message = "An internal server error occurred: " + e.getMessage();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(message);
        }
    }

    @PutMapping("/edit/{id}")
    public ResponseEntity<?> edit(@RequestBody @Valid Genre request,
                                  @PathVariable Long id,
                                  BindingResult result) {
        ResponseEntity<?> validationResponse = Validations.handleValidationErrors(result);
        if (validationResponse != null) {
            return validationResponse;
        }

        try {
            // Find the current genre by ID
            Genre genre = genreService.getById(id);

            // Update the genre fields
            genre.setName(request.getName());

            // Save the updated genre
            genreService.save(genre);

            return ResponseEntity.ok("Genre updated successfully");

        } catch (Exception e) {
            String message = "An internal server error occurred: " + e.getMessage();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(message);
        }
    }

    @Transactional
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id){
        try {
            Genre genre = genreService.getById(id);
            genreService.delete(genre);

            return ResponseEntity.ok("Genre has been deleted successfully");

        } catch (Exception e) {
            String message = "An internal server error occurred: " + e.getMessage();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(message);
        }
    }

}
