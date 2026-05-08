package com.atlasys.freeplanning.planning.controller;

import com.atlasys.freeplanning.identity.model.User;
import com.atlasys.freeplanning.planning.dto.client.ClientDetailWithoutProjectsResponse;
import com.atlasys.freeplanning.planning.dto.client.ClientRequest;
import com.atlasys.freeplanning.planning.dto.client.ClientDetailWithProjectsResponse;
import com.atlasys.freeplanning.planning.dto.client.ClientSummaryResponse;
import com.atlasys.freeplanning.planning.service.ClientService;
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
@RequestMapping("/clients")
public class ClientController {

    private final ClientService service;

    public ClientController(ClientService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<Page<ClientDetailWithoutProjectsResponse>> findAll(
            @AuthenticationPrincipal User loggedUser,
            @RequestParam(required = false) String name,
            @PageableDefault(sort = "name", direction = Sort.Direction.ASC) Pageable pageable
    ) {
        if (name != null) {
            return ResponseEntity.ok(service.findByName(loggedUser, name, pageable));
        }
        return ResponseEntity.ok(service.findAll(loggedUser, pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClientDetailWithProjectsResponse> findOne(
            @AuthenticationPrincipal User loggedUser,
            @PathVariable UUID id
    ) {
       return ResponseEntity.ok(service.findOne(loggedUser, id));
    }

    @PostMapping
    public ResponseEntity<ClientSummaryResponse> create(
            @AuthenticationPrincipal User loggedUser,
            @RequestBody @Valid ClientRequest request
    ) {
        ClientSummaryResponse client = service.create(loggedUser, request);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(client.id())
                .toUri();
        return ResponseEntity.created(uri).body(client);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClientDetailWithoutProjectsResponse> update(
            @AuthenticationPrincipal User loggedUser,
            @RequestBody @Valid ClientRequest request,
            @PathVariable UUID id
    ) {
        return ResponseEntity.ok(service.update(loggedUser, request, id));
    }

    @DeleteMapping("/{id}")
    public void delete(
            @AuthenticationPrincipal User loggedUser,
            @PathVariable UUID id
    ) {
        service.delete(loggedUser, id);
    }
}
