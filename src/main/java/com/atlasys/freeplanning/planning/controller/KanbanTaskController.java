package com.atlasys.freeplanning.planning.controller;

import com.atlasys.freeplanning.identity.model.User;
import com.atlasys.freeplanning.planning.dto.kanban.KanbanTaskRequest;
import com.atlasys.freeplanning.planning.dto.kanban.KanbanTaskResponse;
import com.atlasys.freeplanning.planning.service.KanbanTaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/tasks")
public class KanbanTaskController {

    private final KanbanTaskService service;

    @PostMapping("/{id}")
    public ResponseEntity<KanbanTaskResponse> createTask(
            @AuthenticationPrincipal User loggedUser,
            @PathVariable UUID id,
            @RequestBody KanbanTaskRequest request
    ) {
        return ResponseEntity.ok(service.createTask(loggedUser, id, request));
    }
}
