package com.atlasys.freeplanning.identity.controller;

import com.atlasys.freeplanning.identity.dto.UserResponse;
import com.atlasys.freeplanning.identity.dto.UserUpdateEmailRequest;
import com.atlasys.freeplanning.identity.dto.UserUpdatePasswordRequest;
import com.atlasys.freeplanning.identity.dto.UserUpdateRequest;
import com.atlasys.freeplanning.identity.model.User;
import com.atlasys.freeplanning.identity.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService service;

    @GetMapping
    public ResponseEntity<UserResponse> find(
            @AuthenticationPrincipal User loggedUser
    ) {
        return ResponseEntity.ok(service.find(loggedUser));
    }

    @PatchMapping("/update")
    public ResponseEntity<UserResponse> update(
            @AuthenticationPrincipal User loggedUser,
            @RequestBody UserUpdateRequest request
    ) {
        return ResponseEntity.ok(service.update(loggedUser, request));
    }

    @PutMapping("/change-email")
    public ResponseEntity<UserResponse> changeEmail(
            @AuthenticationPrincipal User loggedUser,
            @RequestBody UserUpdateEmailRequest request
    ) {
        return ResponseEntity.ok(service.changeEmail(loggedUser, request));
    }

    @PutMapping("/change-password")
    public ResponseEntity<Void> changePassword(
            @AuthenticationPrincipal User loggedUser,
            @RequestBody UserUpdatePasswordRequest request
    ) {
        service.changePassword(loggedUser, request);
        return ResponseEntity.noContent().build();
    }
}
