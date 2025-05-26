package com.dailybrief.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.dailybrief.dto.LogRequestDTO;
import com.dailybrief.dto.LogResponseDTO;
import com.dailybrief.mapper.LogMapper;
import com.dailybrief.model.Log;
import com.dailybrief.repository.LogRepository;
import com.dailybrief.service.LogService;

@Service
public class LogServiceImpl implements LogService {

    private static final Logger logger = LoggerFactory.getLogger(LogServiceImpl.class);

    @Autowired
    private LogRepository logRepository;

    @Autowired
    private LogMapper logMapper;

    @Override
    public LogResponseDTO logAction(LogRequestDTO logRequest, String username) {

        Log log = logMapper.toEntity(logRequest);

        log.setCreatedBy(username);

        if (log.getTimestamp() == null) {
            log.setTimestamp(java.time.Instant.now());
        }

        Log savedLog = logRepository.save(log);

        return logMapper.toResponse(savedLog);
    }

    @Override
    public Page<LogResponseDTO> getLogs(Pageable pageable) {

        return logRepository.findAll(pageable).map(logMapper::toResponse);
    }
}