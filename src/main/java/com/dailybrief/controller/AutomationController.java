package com.dailybrief.controller;

import com.dailybrief.dto.MaterialResponseDTO;
import com.dailybrief.service.AutomationService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/automation")
@CrossOrigin(origins = "*")
public class AutomationController {

    private final AutomationService automationService;

    public AutomationController(AutomationService automationService) {
        this.automationService = automationService;
    }

    @Operation(summary = "List all materials", description = "Lists all materials with pagination")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Materials retrieved successfully"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping
    public ResponseEntity<Page<MaterialResponseDTO>> getAllMaterials(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<MaterialResponseDTO> materials = automationService.getAllMaterials(PageRequest.of(page, size));
        return ResponseEntity.ok(materials);
    }

    @Operation(summary = "Get material by taskId", description = "Fetches a material by its taskId")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Material retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Material not found")
    })
    @GetMapping("/{taskId}")
    public ResponseEntity<MaterialResponseDTO> getMaterialById(@PathVariable String taskId) {
        MaterialResponseDTO material = automationService.getMaterialById(taskId);
        return ResponseEntity.ok(material);
    }
}
