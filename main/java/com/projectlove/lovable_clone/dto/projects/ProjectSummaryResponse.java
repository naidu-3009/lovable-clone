package com.projectlove.lovable_clone.dto.projects;

import java.time.Instant;

public record ProjectSummaryResponse(
        Long id,
        String name,
        Instant createdAt,
        Instant updatedAt
) {
}
