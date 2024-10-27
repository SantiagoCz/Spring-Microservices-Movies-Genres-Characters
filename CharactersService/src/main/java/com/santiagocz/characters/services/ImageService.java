package com.santiagocz.characters.services;

import com.santiagocz.characters.entities.Character;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@Service
public class ImageService {

    @Autowired
    private CharacterService characterService;

    public void saveImage(MultipartFile image, Character character) throws IOException {
        // Process image saving
        String imageContentType = image.getContentType();
        String extension = "." + imageContentType.substring(imageContentType.indexOf('/') + 1);
        String photoName = character.getId() + extension;
        String path = Paths.get("src/main/resources/static/images/character", photoName).toAbsolutePath().toString();
        Files.write(Paths.get(path), image.getBytes());

        character.setImage(photoName);
        characterService.update(character);
    }

}
