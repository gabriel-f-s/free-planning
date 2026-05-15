package com.atlasys.freeplanning.planning.dto.project;

import com.atlasys.freeplanning.planning.model.enums.Status;

public record ProjectChangeStatusRequest(
        Status status
) {
}
