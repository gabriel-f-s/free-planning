package com.atlasys.freeplanning.planning.controller;

import com.atlasys.freeplanning.identity.model.User;
import com.atlasys.freeplanning.planning.dto.kanban.*;
import com.atlasys.freeplanning.planning.service.KanbanColumnService;
import com.atlasys.freeplanning.planning.service.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/columns")
public class KanbanColumnController {

    private final KanbanColumnService service;

    @PatchMapping("/{id}/rename")
    public ResponseEntity<KanbanColumnResponse> renameColumn(
            @AuthenticationPrincipal User loggedUser,
            @PathVariable UUID id,
            @RequestBody KanbanColumnRenameRequest name
    ) {
        return ResponseEntity.ok(service.renameColumn(loggedUser, id, name));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteColumn(
            @AuthenticationPrincipal User loggedUser,
            @PathVariable UUID id
    ) {
        service.deleteColumn(loggedUser, id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/tasks")
    public ResponseEntity<KanbanTaskResponse> addTask(
            @AuthenticationPrincipal User loggedUser,
            @PathVariable UUID id,
            @RequestBody KanbanTaskCreateRequest request
    ) {
        return ResponseEntity.ok(service.addTask(loggedUser, id, request));
    }
}
