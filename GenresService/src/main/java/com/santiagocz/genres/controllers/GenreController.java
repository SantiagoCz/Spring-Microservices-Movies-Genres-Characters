package com.santiagocz.genres.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.santiagocz.genres.classes.Validations;
import com.santiagocz.genres.entities.Genre;
import com.santiagocz.genres.services.GenreService;
import com.santiagocz.genres.services.ImageService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/genres")
public class GenreController {

    @Autowired
    private GenreService genreService;

    @Autowired
    private ImageService imageService;

    @Autowired
    private ObjectMapper objectMapper;

    @GetMapping("/all")
    public List<Genre> findAll(){
        return genreService.findAll();
    }

    @GetMapping("/{id}")
    public Genre getById(@PathVariable Long id){
        return genreService.getById(id);
    }

    @Transactional
    @PostMapping("/save")
    public ResponseEntity<?> save(@RequestParam(value = "image", required = false) MultipartFile image,
                                  @RequestParam("genre") String genreJson) {
        try {
            // Convert the received JSON to a Genre object
            Genre request = objectMapper.readValue(genreJson, Genre.class);

            ResponseEntity<?> validationResponse = Validations.validateFields(request);
            if (validationResponse != null) {
                return validationResponse;
            }

            genreService.save(request);

            if (image != null && !image.isEmpty()) {
                imageService.saveImage(image, request);
            }

            return ResponseEntity.ok("Genre has been saved successfully.");
        } catch (Exception e) {
            String message = "An internal server error occurred: " + e.getMessage();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(message);
        }
    }

    @Transactional
    @PutMapping("/update/{id}")
    public ResponseEntity<?> update(@PathVariable Long id,
                                    @RequestParam(value = "image", required = false) MultipartFile image,
                                    @RequestParam("genre") String genreJson) {
        try {
            // Find the current genre by ID
            Genre genre = genreService.getById(id);

            // Convert the received JSON to a Genre object
            Genre request = objectMapper.readValue(genreJson, Genre.class);

            ResponseEntity<?> validationResponse = Validations.validateFields(request);
            if (validationResponse != null) {
                return validationResponse;
            }

            // Check if the name has changed
            if (!genre.getName().equalsIgnoreCase(request.getName())) {
                // If the name has changed, check if the new name already exists
                if (genreService.existsByName(request.getName())) {
                    throw new ResponseStatusException(HttpStatus.CONFLICT, "Genre with this name already exists: " + request.getName());
                }
            }

            // Update the genre fields
            genre.setName(request.getName().toUpperCase());

            genreService.update(genre);

            if (image != null && !image.isEmpty()) {
                imageService.saveImage(image, genre);
            }

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

    @PostMapping("/assign-movie")
    public ResponseEntity<?> assignMovieToGenres(@RequestParam Long movieId, @RequestParam List<Long> genresIds) {
        try {
            // Call the service method to assign the movie to the genres
            genreService.assignMovieToGenres(movieId, genresIds);

            // Return a successful response
            return ResponseEntity.ok("The movie has been successfully assigned to the genres.");

        } catch (IllegalArgumentException e) {
            // Handle invalid argument errors
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            // Handle other internal server errors
            String message = "An internal server error occurred: " + e.getMessage();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(message);
        }
    }

}
