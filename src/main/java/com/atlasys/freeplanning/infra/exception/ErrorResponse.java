package com.atlasys.freeplanning.infra.exception;

import java.time.Instant;

public record ErrorResponse(
    Integer status,
    String error,
    String message,
    String path,
    Instant timestamp
) {
}
