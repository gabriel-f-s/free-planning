package com.atlasys.freeplanning.planning.controller;

import com.atlasys.freeplanning.identity.model.User;
import com.atlasys.freeplanning.planning.dto.AnnotationDTO;
import com.atlasys.freeplanning.planning.dto.kanban.*;
import com.atlasys.freeplanning.planning.dto.project.*;
import com.atlasys.freeplanning.planning.model.Project;
import com.atlasys.freeplanning.planning.service.ProjectService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/projects")
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService service;

    @GetMapping
    public ResponseEntity<Page<ProjectSummaryResponse>> findAll(
            @AuthenticationPrincipal User loggedUser,
            @PageableDefault(sort = "deliveryForecast", direction = Sort.Direction.ASC) Pageable pageable,
            ProjectFilter filter
    ) {
        Page<ProjectSummaryResponse> page = service.findAll(loggedUser, filter, pageable);
        return ResponseEntity.ok(page);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProjectDetailResponse> findOne(
            @AuthenticationPrincipal User loggedUser,
            @PathVariable UUID id
    ) {
        ProjectDetailResponse response = service.findOne(loggedUser, id);
        return ResponseEntity.ok().body(response);
    }

    @PostMapping
    public ResponseEntity<ProjectDetailResponse> create(
            @AuthenticationPrincipal User loggedUser,
            @RequestBody @Valid ProjectCreateRequest request
    ) {
        ProjectDetailResponse response = service.create(loggedUser, request);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(response.id())
                .toUri();
        return ResponseEntity.created(uri).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProjectDetailResponse> update(
            @AuthenticationPrincipal User loggedUser,
            @RequestBody @Valid ProjectUpdateRequest request,
            @PathVariable UUID id
    ) {
        ProjectDetailResponse response = service.update(loggedUser, request, id);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}/notes")
    public ResponseEntity<AnnotationDTO> updateNote(
            @AuthenticationPrincipal User loggedUser,
            @RequestBody @Valid AnnotationDTO annotation,
            @PathVariable UUID id
    ) {
        return ResponseEntity.ok(service.updateNotes(loggedUser, id, annotation));
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<ProjectSummaryResponse> changeStatus(
            @AuthenticationPrincipal User loggedUser,
            @PathVariable UUID id,
            @RequestBody ProjectChangeStatusRequest request
    ) {
        return ResponseEntity.ok(service.changeStatus(loggedUser, id, request));
    }

    @GetMapping("/{id}/columns")
    public ResponseEntity<BoardResponse> findBoard(
            @AuthenticationPrincipal User loggedUser,
            @PathVariable UUID id
    ) {
        return ResponseEntity.ok(service.findBoard(loggedUser, id));
    }

    @PostMapping("/{id}/columns")
    public ResponseEntity<KanbanColumnResponse> addColumn(
            @AuthenticationPrincipal User loggedUser,
            @PathVariable UUID id,
            @RequestBody KanbanColumnCreateRequest column
    ) {
        return ResponseEntity.ok(service.addColumn(loggedUser, id, column));
    }

    @PatchMapping("/{id}/columns/{columnId}/move")
    public ResponseEntity<KanbanColumnResponse> moveColumn(
            @AuthenticationPrincipal User loggedUser,
            @PathVariable UUID id,
            @PathVariable UUID columnId,
            @RequestBody KanbanColumnReorderRequest position
    ) {
        return ResponseEntity.ok(service.moveColumn(loggedUser, id, columnId, position));
    }
}
