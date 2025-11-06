package com.dailybrief.repository;

import com.dailybrief.model.Material;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MaterialRepository extends JpaRepository<Material, String> {
    // Aqui o ID é taskId (String)
}
