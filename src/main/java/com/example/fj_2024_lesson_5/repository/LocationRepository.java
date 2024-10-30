package com.example.fj_2024_lesson_5.repository;

import com.example.fj_2024_lesson_5.entity.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface LocationRepository extends JpaRepository<Location, UUID> {

    @Query("SELECT l FROM Location l LEFT JOIN FETCH l.events WHERE l.id = :id")
    Optional<Location> findByIdWithEvents(@Param("id") UUID id);

}