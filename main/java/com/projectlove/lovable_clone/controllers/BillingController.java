package com.projectlove.lovable_clone.controllers;


import com.projectlove.lovable_clone.Services.PaymentProcessor;
import com.projectlove.lovable_clone.Services.PlanService;
import com.projectlove.lovable_clone.Services.SubscriptionService;
import com.projectlove.lovable_clone.dto.subscription.CheckoutRequest;
import com.projectlove.lovable_clone.dto.subscription.CheckoutResponse;
import com.projectlove.lovable_clone.dto.subscription.PlanResponse;
import com.projectlove.lovable_clone.dto.subscription.PortalResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true,level = AccessLevel.PRIVATE)
public class BillingController {
     PlanService planService;
     SubscriptionService subscriptionService;
     PaymentProcessor paymentProcessor;

    @GetMapping("/api/plans")
    public ResponseEntity<List<PlanResponse>> getAllPlans(){
        return ResponseEntity.ok(planService.getAllActivePlans());
    }

    @GetMapping("/api/me/subscription")
    public ResponseEntity<SubscriptionService> getMySubscription(){
    return ResponseEntity.ok(subscriptionService.getCurrentSubscription());
    }

    @PostMapping("/api/payment/checkout")
    public ResponseEntity<CheckoutResponse> createCheckoutResponse(
            @RequestBody CheckoutRequest checkoutRequest
    ){
        return ResponseEntity.ok(paymentProcessor.createCheckoutSessionUrl(checkoutRequest));
    }

    @PostMapping("/api/payment/portal")
    public ResponseEntity<PortalResponse> openCustomerPortal(){
        return ResponseEntity.ok(paymentProcessor.openCustomerPortal());
    }
}
