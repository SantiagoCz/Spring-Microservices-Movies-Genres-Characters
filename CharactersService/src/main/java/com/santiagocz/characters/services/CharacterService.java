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

    public Character save(Character character){
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
}
