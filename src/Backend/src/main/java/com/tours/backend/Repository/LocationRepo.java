package com.tours.backend.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LocationRepo extends JpaRepository<LocationRepo, Long> {
    LocationRepo findTopByOrderByIdDesc();
}
