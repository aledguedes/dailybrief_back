package com.dailybrief.repository;

import com.dailybrief.model.RawMaterial;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RawMaterialRepository extends JpaRepository<RawMaterial, String> {
    List<RawMaterial> findByUserId(String userId);

    List<RawMaterial> findAllByIdIn(List<String> ids);

    List<RawMaterial> findByContentContainingIgnoreCaseOrUrlContainingIgnoreCase(String contentQuery, String urlQuery);
}
