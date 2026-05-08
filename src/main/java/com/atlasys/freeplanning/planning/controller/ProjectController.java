package com.atlasys.freeplanning.planning.controller;

import com.atlasys.freeplanning.identity.model.User;
import com.atlasys.freeplanning.planning.dto.Note;
import com.atlasys.freeplanning.planning.dto.kanban.BoardResponse;
import com.atlasys.freeplanning.planning.dto.project.*;
import com.atlasys.freeplanning.planning.service.ProjectService;
import jakarta.validation.Valid;
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
public class ProjectController {

    private final ProjectService service;

    public ProjectController(ProjectService service) {
        this.service = service;
    }

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

    @GetMapping("/{id}/board")
    public ResponseEntity<BoardResponse> findBoard(
            @AuthenticationPrincipal User loggedUser,
            @PathVariable UUID id
    ) {
        return ResponseEntity.ok(service.findProjectBoard(loggedUser, id));
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
    public ResponseEntity<Note> updateNote(
            @AuthenticationPrincipal User loggedUser,
            @RequestBody @Valid Note annotation,
            @PathVariable UUID id
    ) {
        return ResponseEntity.ok(service.updateNotes(loggedUser, id, annotation));
    }
}
