package com.santiagocz.movies.services;

import com.santiagocz.movies.entities.Movie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@Service
public class ImageService {

    @Autowired
    private MovieService movieService;

    public void saveImage(MultipartFile image, Movie movie) throws IOException {
        // Process image saving
        String imageContentType = image.getContentType();
        String extension = "." + imageContentType.substring(imageContentType.indexOf('/') + 1);
        String photoName = movie.getId() + extension;
        String path = Paths.get("src/main/resources/static/images/movie", photoName).toAbsolutePath().toString();
        Files.write(Paths.get(path), image.getBytes());

        movie.setImage(photoName);
        movieService.update(movie);
    }
}
