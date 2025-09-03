package com.dailybrief.dto;

public record PythonTriggerResponseDTO(
    Integer trigger_id,
    String message,
    String task_id,
    String status
) {}