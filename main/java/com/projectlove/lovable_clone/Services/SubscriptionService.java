package com.projectlove.lovable_clone.Services;

import com.projectlove.lovable_clone.dto.subscription.CheckoutRequest;
import com.projectlove.lovable_clone.dto.subscription.CheckoutResponse;
import com.projectlove.lovable_clone.dto.subscription.PortalResponse;
import org.jspecify.annotations.Nullable;

public interface SubscriptionService {
     SubscriptionService getCurrentSubscription(Long userId);

    CheckoutResponse createCheckoutSessionUrl(CheckoutRequest checkoutRequest, Long userId);

    PortalResponse openCustomerPortal(Long userId);
}
