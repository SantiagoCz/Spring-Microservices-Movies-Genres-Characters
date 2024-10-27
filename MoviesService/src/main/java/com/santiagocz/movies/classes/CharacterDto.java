package com.santiagocz.movies.classes;

import jakarta.persistence.ElementCollection;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CharacterDto {

    private Long id;
    private String name;
    private int age;
    private String image;
    private String story;
    @ElementCollection
    private List<Long> moviesIds;

}
