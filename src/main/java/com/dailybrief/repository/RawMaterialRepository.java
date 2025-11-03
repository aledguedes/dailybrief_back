package com.dailybrief.repository;

import com.dailybrief.model.RawMaterial;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RawMaterialRepository extends JpaRepository<RawMaterial, String> {

    // Exemplo de consulta: busca todos por taskId (FK de Material)
    List<RawMaterial> findByMaterial_TaskId(String taskId);

    // Exemplo de consulta: busca todos por userId
    List<RawMaterial> findByUserId(String userId);
    
    List<RawMaterial> findAllByIdIn(List<String> ids);
}
