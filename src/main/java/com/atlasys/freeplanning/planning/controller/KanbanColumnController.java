package com.atlasys.freeplanning.planning.controller;

import com.atlasys.freeplanning.identity.model.User;
import com.atlasys.freeplanning.planning.dto.kanban.*;
import com.atlasys.freeplanning.planning.service.KanbanColumnService;
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

    @PostMapping("/{id}")
    public ResponseEntity<KanbanColumnResponse> createColumn(
            @AuthenticationPrincipal User loggedUser,
            @PathVariable UUID id,
            @RequestBody KanbanColumnRequest column
    ) {
        return ResponseEntity.ok(service.createColumn(loggedUser, id, column));
    }

    @PutMapping("/{id}")
    public ResponseEntity<KanbanColumnResponse> updateColumn(
            @AuthenticationPrincipal User loggedUser,
            @PathVariable UUID id,
            @RequestBody KanbanColumnRequest column
    ) {
        return ResponseEntity.ok(service.updateColumn(loggedUser, id, column));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteColumn(
            @AuthenticationPrincipal User loggedUser,
            @PathVariable UUID id
    ) {
        service.deleteColumn(loggedUser, id);
        return ResponseEntity.noContent().build();
    }
}
