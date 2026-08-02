package com.projectlove.lovable_clone.Services.impl;

import com.projectlove.lovable_clone.Services.UsageService;
import com.projectlove.lovable_clone.dto.subscription.PlanLimitResponse;
import com.projectlove.lovable_clone.dto.subscription.UsageTodayResponse;
import org.springframework.stereotype.Service;


@Service
public class UsageServiceImpl implements UsageService {
    @Override
    public UsageTodayResponse getTodayUsageOfUser(Long userId) {
        return null;
    }

    @Override
    public PlanLimitResponse getCurrentSubscriptionLimits(Long userId) {
        return null;
    }
}
