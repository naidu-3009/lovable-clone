package com.projectlove.lovable_clone.dto.subscription;

public record PlanLimitResponse(
        String planName,
        Integer maxTokensPerPolicy,
        Integer maxProjects,
        boolean unlimitedAi
) {
}
