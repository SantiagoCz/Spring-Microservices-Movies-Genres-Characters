package com.santiagocz.characters.services;

import com.santiagocz.characters.entities.Character;
import com.santiagocz.characters.repositories.CharacterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class CharacterService {

    @Autowired
    private CharacterRepository characterRepository;

    public Character save(Character character) {
        character.setName(character.getName().toUpperCase());
        if (existsByName(character.getName())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Character with this name already exists: " + character.getName());
        }
        return characterRepository.save(character);
    }

    public Character update(Character character) {
        return characterRepository.save(character);
    }

    public void delete(Character character){
        characterRepository.delete(character);
    }

    public List<Character> findAll(){
        return characterRepository.findAll();
    }

    public Character getById(Long id){
        return characterRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Character not found with ID: " + id));
    }

    public boolean existsByName(String name) {
        return characterRepository.existsByName(name);
    }

    // Method to assign a movie to multiple characters
    public void assignMovieToCharacters(Long movieId, List<Long> charactersIds) {
        // Iterate over the IDs of the characters to assign them to the movie
        for (Long characterId : charactersIds) {
            Character character = getById(characterId);

            // Verify if the character is already assigned to the movie
            if (!character.getMoviesIds().contains(movieId)) {
                character.getMoviesIds().add(movieId);
                characterRepository.save(character);
            }
        }
    }
}
