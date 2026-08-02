package com.projectlove.lovable_clone.Services.impl;

import com.projectlove.lovable_clone.Services.SubscriptionService;
import com.projectlove.lovable_clone.dto.subscription.CheckoutRequest;
import com.projectlove.lovable_clone.dto.subscription.CheckoutResponse;
import com.projectlove.lovable_clone.dto.subscription.PortalResponse;
import org.springframework.stereotype.Service;

@Service
public class SubscriptionServiceImpl implements SubscriptionService {
    @Override
    public SubscriptionService getCurrentSubscription(Long userId) {
        return null;
    }

    @Override
    public CheckoutResponse createCheckoutSessionUrl(CheckoutRequest checkoutRequest, Long userId) {
        return null;
    }

    @Override
    public PortalResponse openCustomerPortal(Long userId) {
        return null;
    }
}
