package com.atlasys.freeplanning.planning.controller;

import com.atlasys.freeplanning.identity.model.User;
import com.atlasys.freeplanning.planning.dto.kanban.KanbanTaskReorderRequest;
import com.atlasys.freeplanning.planning.dto.kanban.KanbanTaskUpdateRequest;
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

    @PatchMapping("/{id}/move")
    public ResponseEntity<KanbanTaskResponse> moveTask(
            @AuthenticationPrincipal User loggedUser,
            @PathVariable UUID id,
            @RequestBody KanbanTaskReorderRequest request
    ) {
        return ResponseEntity.ok(service.moveTask(loggedUser, id, request));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<KanbanTaskResponse> updateTask(
            @AuthenticationPrincipal User loggedUser,
            @PathVariable UUID id,
            @RequestBody KanbanTaskUpdateRequest request
    ) {
        return ResponseEntity.ok(service.updateTask(loggedUser, id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(
            @AuthenticationPrincipal User loggedUser,
            @PathVariable UUID id
    ) {
        service.deleteTask(loggedUser, id);
        return ResponseEntity.noContent().build();
    }

}
