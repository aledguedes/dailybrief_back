package com.dailybrief.repository;

import com.dailybrief.model.Log;

import java.time.Instant;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface LogRepository extends JpaRepository<Log, Long> {
	List<Log> findByCreatedBy(String createdBy);

    List<Log> findByTimestampAfter(Instant startDate);
}