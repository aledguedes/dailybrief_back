package com.dailybrief.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.UUID;

@Entity
@Table(name = "tbl_trigger_tasks")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TriggerTask {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private UUID triggerId;
    private String taskId;
    private String message;
    private String status;
}