package com.projectlove.lovable_clone.Services;

import com.projectlove.lovable_clone.dto.subscription.CheckoutRequest;
import com.projectlove.lovable_clone.dto.subscription.CheckoutResponse;
import com.projectlove.lovable_clone.dto.subscription.PortalResponse;

public interface PaymentProcessor {
    CheckoutResponse createCheckoutSessionUrl(CheckoutRequest checkoutRequest);

    PortalResponse openCustomerPortal();
}
