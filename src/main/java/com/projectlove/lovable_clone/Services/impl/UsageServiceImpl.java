package com.projectlove.lovable_clone.Services.impl;

import com.projectlove.lovable_clone.Services.SubscriptionService;
import com.projectlove.lovable_clone.Services.UsageService;
import com.projectlove.lovable_clone.dto.subscription.PlanLimitResponse;
import com.projectlove.lovable_clone.dto.subscription.PlanResponse;
import com.projectlove.lovable_clone.dto.subscription.SubscriptionResponse;
import com.projectlove.lovable_clone.dto.subscription.UsageTodayResponse;
import com.projectlove.lovable_clone.entity.UsageLog;
import com.projectlove.lovable_clone.repository.UsageLogRepository;
import com.projectlove.lovable_clone.security.AuthUtil;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.jspecify.annotations.Nullable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;

@RequiredArgsConstructor
@Service
@FieldDefaults(makeFinal = true,level = AccessLevel.PRIVATE)
public class UsageServiceImpl implements UsageService {

    UsageLogRepository usageLogRepository;
    AuthUtil authUtil;
    SubscriptionService subscriptionService;

    @Override
    public void checkDailyTokensUsage() {
        Long userId = authUtil.getCurrentUserId();
        SubscriptionResponse subscriptionResponse = subscriptionService.getCurrentSubscription();
        PlanResponse plan = subscriptionResponse.plan();

        LocalDate today = LocalDate.now();

        UsageLog todayLog = usageLogRepository.findByUserIdAndDate(userId, today).
                orElseGet(() -> createNewDailyLog(userId, today));

        if(plan.unlimitedAi()) return;

        int currentUsage = todayLog.getTokensUsed();
        int limit = plan.maxTokensPerDay();

        if(currentUsage >=  limit) {
            throw new ResponseStatusException(HttpStatus.TOO_MANY_REQUESTS,
                    "Daily limit reached, Upgrade now");
        }

    }

    @Override
    public void recordTokenUsage(Long userId, int actualTokens) {
        LocalDate today = LocalDate.now();

        UsageLog todayLog = usageLogRepository.findByUserIdAndDate(userId, today).
                orElseGet(() -> createNewDailyLog(userId, today));

        todayLog.setTokensUsed(todayLog.getTokensUsed() + actualTokens);
        usageLogRepository.save(todayLog);
    }

    @Override
    public @Nullable PlanLimitResponse getCurrentSubscriptionLimits(Long userId) {
        return null;
    }

    @Override
    public @Nullable UsageTodayResponse getTodayUsageOfUser(Long userId) {
        return null;
    }


    private UsageLog createNewDailyLog(Long userId, LocalDate date) {
        UsageLog newLog = UsageLog.builder()
                .userId(userId)
                .date(date)
                .tokensUsed(0)
                .build();
        return usageLogRepository.save(newLog);
    }
}
