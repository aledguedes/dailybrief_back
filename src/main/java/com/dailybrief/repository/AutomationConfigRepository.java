package com.dailybrief.repository;

import com.dailybrief.model.AutomationConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AutomationConfigRepository extends JpaRepository<AutomationConfig, String> {
    // taskId é a PK (String)
}
