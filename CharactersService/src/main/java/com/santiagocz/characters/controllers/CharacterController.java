package com.santiagocz.characters.controllers;

import com.santiagocz.characters.classes.Validations;
import com.santiagocz.characters.entities.Character;
import com.santiagocz.characters.services.CharacterService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/characters")
public class CharacterController {

    @Autowired
    private CharacterService characterService;

    @GetMapping("/all")
    public List<Character> findAll(){
        return characterService.findAll();
    }

    @GetMapping("/{id}")
    public Character getById(@PathVariable Long id){
        return characterService.getById(id);
    }

    @PostMapping("/save")
    public ResponseEntity<?> save(@RequestBody @Valid Character request,
                                  BindingResult result){
        ResponseEntity<?> validationResponse = Validations.handleValidationErrors(result);
        if (validationResponse != null) {
            return validationResponse;
        }
        try {
            characterService.save(request);
            return ResponseEntity.ok("Genre has been saved succesfully.");
        } catch (Exception e) {
            String message = "An internal server error occurred: " + e.getMessage();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(message);
        }
    }

    @PutMapping("/edit/{id}")
    public ResponseEntity<?> edit(@RequestBody @Valid Character request,
                                  @PathVariable Long id,
                                  BindingResult result) {
        ResponseEntity<?> validationResponse = Validations.handleValidationErrors(result);
        if (validationResponse != null) {
            return validationResponse;
        }

        try {
            // Find the current character by ID
            Character character = characterService.getById(id);

            // Update the character fields
            character.setName(request.getName());
            character.setAge(request.getAge());
            character.setStory(request.getStory());

            // Save the updated character
            characterService.save(character);

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

}
