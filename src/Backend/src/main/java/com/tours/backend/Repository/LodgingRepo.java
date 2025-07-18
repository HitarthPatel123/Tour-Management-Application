package com.tours.backend.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LodgingRepo extends JpaRepository<LodgingRepo, Long> {
    LodgingRepo findTopByOrderByIdDesc();
}
