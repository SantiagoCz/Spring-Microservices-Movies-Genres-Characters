package com.santiagocz.genres.services;

import com.santiagocz.genres.entities.Genre;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@Service
public class ImageService {

    @Autowired
    private GenreService genreService;

    public void saveImage(MultipartFile image, Genre genre) throws IOException {
        // Process image saving
        String imageContentType = image.getContentType();
        String extension = "." + imageContentType.substring(imageContentType.indexOf('/') + 1);
        String photoName = genre.getId() + extension;
        String path = Paths.get("src/main/resources/static/images/genre", photoName).toAbsolutePath().toString();
        Files.write(Paths.get(path), image.getBytes());

        genre.setImage(photoName);
        genreService.update(genre);
    }

}
