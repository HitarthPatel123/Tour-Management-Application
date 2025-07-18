package com.tours.backend.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransportRepo extends JpaRepository<TransportRepo, Integer> {
    TransportRepo findTopByOrderByIdDesc();
}
