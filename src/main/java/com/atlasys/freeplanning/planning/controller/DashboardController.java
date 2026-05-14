package com.atlasys.freeplanning.planning.controller;

import com.atlasys.freeplanning.identity.model.User;
import com.atlasys.freeplanning.planning.dto.AnnotationDTO;
import com.atlasys.freeplanning.planning.dto.dashboard.DashboardPipelineResponse;
import com.atlasys.freeplanning.planning.dto.dashboard.DashboardSummaryResponse;
import com.atlasys.freeplanning.planning.service.DashboardService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/dashboard")
public class DashboardController {

    private final DashboardService service;

    public DashboardController(DashboardService service) {
        this.service = service;
    }

    @GetMapping("/summary")
    public ResponseEntity<DashboardSummaryResponse> summary(
            @AuthenticationPrincipal User loggedUser
    ) {
        return ResponseEntity.ok(service.summary(loggedUser));
    }

    @GetMapping("/pipeline")
    public ResponseEntity<DashboardPipelineResponse> pipeline(
            @AuthenticationPrincipal User loggedUser
    ) {
        return ResponseEntity.ok(service.pipeline(loggedUser));
    }

    @GetMapping("/notes")
    public ResponseEntity<AnnotationDTO> findAnnotation(
            @AuthenticationPrincipal User loggedUser
    ) {
        return ResponseEntity.ok(service.findAnnotation(loggedUser));
    }

    @PutMapping(value = "/notes")
    public ResponseEntity<AnnotationDTO> updateAnnotation(
            @AuthenticationPrincipal User loggedUser,
            @RequestBody AnnotationDTO annotation
    ) {
        return ResponseEntity.ok(service.updateAnnotation(loggedUser, annotation));
    }

    @GetMapping("/hourly-rate")
    public ResponseEntity<BigDecimal> findUserHourlyRate(
            @AuthenticationPrincipal User loggedUser
    ) {
        return ResponseEntity.ok(service.findUserHourlyRate(loggedUser));
    }
}
