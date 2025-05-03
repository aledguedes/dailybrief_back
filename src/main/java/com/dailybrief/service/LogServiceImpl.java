package com.dailybrief.service;

import com.dailybrief.dto.LogRequestDTO;
import com.dailybrief.dto.LogResponseDTO;
import com.dailybrief.model.Log;
import com.dailybrief.repository.LogRepository;
import com.dailybrief.mapper.LogMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
public class LogServiceImpl implements LogService {

    @Autowired
    private LogRepository logRepository;

    @Autowired
    private LogMapper logMapper;

    @Override
    public LogResponseDTO logAction(LogRequestDTO logRequest) {
        Log log = logMapper.toEntity(logRequest);
        log.setTimestamp(Instant.now());
        Log savedLog = logRepository.save(log);
        return logMapper.toResponse(savedLog);
    }

    @Override
    public List<LogResponseDTO> getLogs() {
        return logMapper.toResponseList(logRepository.findAll());
    }
}