package com.santiagocz.characters.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.santiagocz.characters.classes.Validations;
import com.santiagocz.characters.entities.Character;
import com.santiagocz.characters.services.CharacterService;
import com.santiagocz.characters.services.ImageService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/characters")
public class CharacterController {

    @Autowired
    private CharacterService characterService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ImageService imageService;

    @GetMapping("/all")
    public List<Character> findAll(){
        return characterService.findAll();
    }

    @GetMapping("/{id}")
    public Character getById(@PathVariable Long id){
        return characterService.getById(id);
    }

    @Transactional
    @PostMapping("/save")
    public ResponseEntity<?> save(@RequestParam(value = "image", required = false) MultipartFile image,
                                  @RequestParam("character") String characterJson) {
        try {
            // Convert the received JSON to a Character object
            Character request = objectMapper.readValue(characterJson, Character.class);

            ResponseEntity<?> validationResponse = Validations.validateFields(request);
            if (validationResponse != null) {
                return validationResponse;
            }

            characterService.save(request);

            if (image != null && !image.isEmpty()) {
                imageService.saveImage(image, request);
            }

            return ResponseEntity.ok("Character has been saved successfully.");
        } catch (Exception e) {
            String message = "An internal server error occurred: " + e.getMessage();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(message);
        }
    }

    @Transactional
    @PutMapping("/update/{id}")
    public ResponseEntity<?> update(@PathVariable Long id,
                                    @RequestParam(value = "image", required = false) MultipartFile image,
                                    @RequestParam("character") String characterJson) {
        try {
            // Find the current character by ID
            Character character = characterService.getById(id);

            // Convert the received JSON to a Character object
            Character request = objectMapper.readValue(characterJson, Character.class);

            ResponseEntity<?> validationResponse = Validations.validateFields(request);
            if (validationResponse != null) {
                return validationResponse;
            }

            // Check if the name has changed
            if (!character.getName().equalsIgnoreCase(request.getName())) {
                // If the name has changed, check if the new name already exists
                if (characterService.existsByName(request.getName())) {
                    throw new ResponseStatusException(HttpStatus.CONFLICT, "Character with this name already exists: " + request.getName());
                }
            }

            // Update the character fields
            character.setName(request.getName().toUpperCase());
            character.setAge(request.getAge());
            character.setStory(request.getStory());

            characterService.update(character);

            if (image != null && !image.isEmpty()) {
                imageService.saveImage(image, character);
            }

            return ResponseEntity.ok("Character updated successfully");
        } catch (Exception e) {
            String message = "An internal server error occurred: " + e.getMessage();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(message);
        }
    }

    @Transactional
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id){
        try {
            Character character = characterService.getById(id);
            characterService.delete(character);

            return ResponseEntity.ok("Character has been deleted successfully");

        } catch (Exception e) {
            String message = "An internal server error occurred: " + e.getMessage();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(message);
        }
    }

    @PostMapping("/assign-movie")
    public ResponseEntity<?> assignMovieToCharacters(@RequestParam Long movieId, @RequestParam List<Long> charactersIds) {
        try {
            // Call the service method to assign the movie to the characters
            characterService.assignMovieToCharacters(movieId, charactersIds);

            // Return a successful response
            return ResponseEntity.ok("The movie has been successfully assigned to the characters.");

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
