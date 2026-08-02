package com.projectlove.lovable_clone.dto.subscription;

import jakarta.persistence.criteria.CriteriaBuilder;

public record UsageTodayResponse(
        Integer tokensUsed, Integer tokensLimit,Integer previewsRunning,Integer previewsLimit
        ) {
}
