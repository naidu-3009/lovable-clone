package com.projectlove.lovable_clone.Services;

import com.projectlove.lovable_clone.dto.subscription.SubscriptionResponse;
import com.projectlove.lovable_clone.enums.SubscriptionStatus;

import java.time.Instant;

public interface SubscriptionService {
     SubscriptionResponse getCurrentSubscription();

    void activateSubscription(Long userId, Long planId, String subscriptionId, String customerId);

    void updateSubscription(String gatewaySubscriptionId, SubscriptionStatus status, Instant periodStart, Instant periodEnd, Boolean cancelAtPeriodEnd, Long planId);

    void cancelSubscription(String gatewaySubscriptionId);

    void renewSubscriptionPeriod(String subscriptionId, Instant periodStart, Instant periodEnd);

    void markSubscriptionPastDue(String subscriptionId);

    boolean canCreateNewProject();
}
