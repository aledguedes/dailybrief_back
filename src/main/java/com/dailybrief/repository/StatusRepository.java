package com.dailybrief.repository;

import com.dailybrief.model.Status;

import org.springframework.data.jpa.repository.JpaRepository;

public interface StatusRepository extends JpaRepository<Status, Integer> {
    
}
