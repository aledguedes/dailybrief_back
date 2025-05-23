package com.dailybrief.repository;

import com.dailybrief.model.Automation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AutomationRepository extends JpaRepository<Automation, Long> {
}