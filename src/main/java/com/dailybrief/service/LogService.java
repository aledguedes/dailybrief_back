package com.dailybrief.service;

import com.dailybrief.dto.LogRequestDTO;
import com.dailybrief.dto.LogResponseDTO;

import java.util.List;

public interface LogService {
    LogResponseDTO logAction(LogRequestDTO logRequest);

    List<LogResponseDTO> getLogs();
}
