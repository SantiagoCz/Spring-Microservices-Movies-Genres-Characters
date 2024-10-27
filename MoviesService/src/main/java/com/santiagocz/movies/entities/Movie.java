package com.santiagocz.movies.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import jakarta.validation.constraints.*;
import java.time.LocalDate;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "movie")
public class Movie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String image;

    @NotBlank(message = "Title is mandatory")
    @Size(max = 255, message = "Title can't exceed 255 characters")
    @Column(unique = true)
    private String title;

    @NotNull(message = "Creation date is mandatory")
    private LocalDate creationDate;

    @Min(value = 1, message = "Rating must be at least 1")
    @Max(value = 5, message = "Rating must be at most 5")
    private Integer rating;

    @ElementCollection
    private List<Long> charactersIds;

    @ElementCollection
    private List<Long> genresIds;
}

