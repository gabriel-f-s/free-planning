package com.atlasys.freeplanning.planning.dto.project;

public record ProjectFilter(
        String title,
        String platform,
        String status,
        String type,
        String deliveryForecast,
        String deliveryDate
) {
}
