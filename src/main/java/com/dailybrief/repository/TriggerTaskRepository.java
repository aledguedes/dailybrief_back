package com.dailybrief.repository;

import com.dailybrief.model.TriggerTask;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface TriggerTaskRepository extends JpaRepository<TriggerTask, UUID> {
}