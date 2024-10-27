package com.santiagocz.movies.repositories;

import com.santiagocz.movies.entities.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Long> {
    boolean existsByTitle(String title);
}
