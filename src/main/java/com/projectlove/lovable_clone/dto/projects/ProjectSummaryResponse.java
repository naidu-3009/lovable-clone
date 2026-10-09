package com.projectlove.lovable_clone.dto.projects;

import com.projectlove.lovable_clone.enums.ProjectMemberRole;

import java.time.Instant;

public record ProjectSummaryResponse(
        Long projectId,
        String name,
        Instant createdAt,
        Instant updatedAt,
        ProjectMemberRole role
) {
}
