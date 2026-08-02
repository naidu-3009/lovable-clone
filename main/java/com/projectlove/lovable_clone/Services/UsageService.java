package com.projectlove.lovable_clone.Services;

import com.projectlove.lovable_clone.dto.subscription.PlanLimitResponse;
import com.projectlove.lovable_clone.dto.subscription.PlanResponse;
import com.projectlove.lovable_clone.dto.subscription.UsageTodayResponse;
import org.jspecify.annotations.Nullable;

public interface UsageService {
     UsageTodayResponse getTodayUsageOfUser(Long userId);

    PlanLimitResponse getCurrentSubscriptionLimits(Long userId);
}
