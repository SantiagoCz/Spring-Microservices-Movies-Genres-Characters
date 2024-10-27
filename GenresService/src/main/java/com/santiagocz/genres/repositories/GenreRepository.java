package com.santiagocz.genres.repositories;

import com.santiagocz.genres.entities.Genre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface GenreRepository extends JpaRepository<Genre, Long> {
    @Query("SELECT CASE WHEN COUNT(g) > 0 THEN true ELSE false END FROM Genre g WHERE UPPER(g.name) = UPPER(:name)")
    boolean existsByName(@Param("name") String name);
}
