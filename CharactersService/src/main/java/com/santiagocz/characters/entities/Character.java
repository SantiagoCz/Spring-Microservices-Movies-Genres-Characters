package com.santiagocz.characters.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "character_entity")
public class Character {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Name is mandatory")
    @Column(unique = true)
    private String name;

    @NotNull(message = "Age is mandatory")
    @Positive(message = "Age must be a positive number")
    private int age;

    private String image;

    @NotBlank(message = "Story is mandatory")
    @Size(max = 255, message = "Story can't exceed 255 characters")
    private String story;

    @ElementCollection
    private List<Long> moviesIds;

}
