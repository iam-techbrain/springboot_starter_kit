package com.example.starterapi.common.controller;

import com.example.starterapi.common.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/health")
@Tag(name = "Health", description = "Application liveness and readiness probe")
public class HealthController {

    @GetMapping
    @Operation(summary = "Check API server status")
    public ResponseEntity<ApiResponse<Map<String, Object>>> checkHealth() {
        Map<String, Object> status = Map.of(
                "status", "UP",
                "timestamp", Instant.now(),
                "service", "Spring Boot Starter Kit API",
                "version", "1.0.0"
        );
        return ResponseEntity.ok(ApiResponse.success("Service is healthy and running", status));
    }
}
