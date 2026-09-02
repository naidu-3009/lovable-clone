package com.projectlove.lovable_clone.Services;

import com.projectlove.lovable_clone.dto.subscription.PlanLimitResponse;
import com.projectlove.lovable_clone.dto.subscription.PlanResponse;
import com.projectlove.lovable_clone.dto.subscription.UsageTodayResponse;
import org.jspecify.annotations.Nullable;

public interface UsageService {

    void checkDailyTokensUsage();
    void recordTokenUsage(Long id, int totalTokens);

    @Nullable PlanLimitResponse getCurrentSubscriptionLimits(Long userId);

    @Nullable UsageTodayResponse getTodayUsageOfUser(Long userId);
}

