package com.dailybrief.service;

import com.dailybrief.dto.LogRequestDTO;
import com.dailybrief.dto.LogResponseDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface LogService {
    LogResponseDTO logAction(LogRequestDTO logRequest);

    Page<LogResponseDTO> getLogs(Pageable pageable);
}
