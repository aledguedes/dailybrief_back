package com.dailybrief.service.impl;

import com.dailybrief.dto.LogRequestDTO;
import com.dailybrief.dto.LogResponseDTO;
import com.dailybrief.mapper.LogMapper;
import com.dailybrief.model.Log;
import com.dailybrief.repository.LogRepository;
import com.dailybrief.service.LogService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class LogServiceImpl implements LogService {

    @Autowired
    private LogRepository logRepository;

    @Autowired
    private LogMapper logMapper;

    @Override
    public LogResponseDTO logAction(LogRequestDTO logRequest, String username) {
        Log log = logMapper.toEntity(logRequest);
        log.setCreatedBy(username);
        log.setTimestamp(logRequest.timestamp() != null ? logRequest.timestamp() : java.time.Instant.now());
        Log savedLog = logRepository.save(log);
        return logMapper.toResponse(savedLog);
    }

    @Override
    public Page<LogResponseDTO> getLogs(Pageable pageable) {
        return logRepository.findAll(pageable).map(logMapper::toResponse);
    }
}