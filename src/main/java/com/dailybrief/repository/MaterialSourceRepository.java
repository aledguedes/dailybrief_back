package com.dailybrief.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dailybrief.model.MaterialSource;

public interface MaterialSourceRepository extends JpaRepository<MaterialSource, UUID> {
	Optional<MaterialSource> findByRawMaterialId(String rawMaterialId);
    List<MaterialSource> findByMaterial_TaskIdOrderByCreatedAtAsc(String taskId);
}
