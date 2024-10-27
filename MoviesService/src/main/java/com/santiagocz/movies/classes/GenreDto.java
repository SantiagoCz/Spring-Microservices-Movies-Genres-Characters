package com.santiagocz.movies.classes;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GenreDto {

    private Long id;
    private String name;
    private String image;
    @ElementCollection
    private List<Long> moviesIds;

}
