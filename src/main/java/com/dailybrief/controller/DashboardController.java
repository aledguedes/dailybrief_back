package com.dailybrief.controller;

import com.dailybrief.dto.dashboard.DashboardResponseDTO;
import com.dailybrief.service.DashboardService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dashboard")
@CrossOrigin(origins = "*")
public class DashboardController {

    @Autowired
    private DashboardService dashboardService;

    @GetMapping
    public ResponseEntity<DashboardResponseDTO> getDashboardData() {
        DashboardResponseDTO dashboardData = dashboardService.getDashboardData();
        return ResponseEntity.ok(dashboardData);
    }
}