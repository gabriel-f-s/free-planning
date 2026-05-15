package com.atlasys.freeplanning.identity.dto;

import jakarta.validation.constraints.Size;

public record UserUpdatePasswordRequest(
        String oldPassword,
        @Size(min = 8, message = "The new password must be at least 8 characters long") String newPassword,
        String confirmNewPassword
) {
}
