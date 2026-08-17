package com.projectlove.lovable_clone.Services.impl;

import com.projectlove.lovable_clone.Services.PaymentProcessor;
import com.projectlove.lovable_clone.dto.subscription.CheckoutRequest;
import com.projectlove.lovable_clone.dto.subscription.CheckoutResponse;
import com.projectlove.lovable_clone.dto.subscription.PortalResponse;
import com.projectlove.lovable_clone.entity.Plan;
import com.projectlove.lovable_clone.entity.User;
import com.projectlove.lovable_clone.error.ResourceNotFoundException;
import com.projectlove.lovable_clone.repository.PlanRepository;
import com.projectlove.lovable_clone.repository.UserRepository;
import com.projectlove.lovable_clone.security.AuthUtil;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
//@FieldDefaults(makeFinal = true,level = AccessLevel.PRIVATE->should not use as we want frontEndUrl flexible to change
public class StripePaymentProcessor implements PaymentProcessor {

    private final AuthUtil authUtil;
    private final UserRepository userRepository;
    private final PlanRepository planRepository;


    @Value("${client.url}")
    private String frontEndUrl;

    @Override
    public CheckoutResponse createCheckoutSessionUrl(CheckoutRequest checkoutRequest) {
        Plan plan=planRepository.findById(checkoutRequest.planId()).orElseThrow(()-> new ResourceNotFoundException("Plan",checkoutRequest.planId().toString()));
        Long userId=authUtil.getCurrentUserId();

        User user=userRepository.findById(userId).orElseThrow(()->new ResourceNotFoundException("user",userId.toString()));

        var params = SessionCreateParams.builder()
                .addLineItem(
                        SessionCreateParams.LineItem.builder()
                                .setPrice(plan.getStripePriceId())
                                .setQuantity(1L)
                                .build())
                .setMode(SessionCreateParams.Mode.SUBSCRIPTION)
                .setSubscriptionData(
                        SessionCreateParams.SubscriptionData.builder()
                                .setBillingMode(
                                        SessionCreateParams.SubscriptionData.BillingMode.builder()
                                                .setType(SessionCreateParams.SubscriptionData.BillingMode.Type.FLEXIBLE) //this flexible allows users to subscribe anyday of month and would be charged 30days/defined period from then
                                                .build())
                                .putMetadata("user_id", userId.toString())
                                .putMetadata("plan_id", plan.getId().toString())
                                .build())
                .setSuccessUrl(frontEndUrl + "/success.html?session_id={CHECKOUT_SESSION_ID}")
                .setCancelUrl(frontEndUrl + "/cancel.html")
                .putMetadata("user_id", userId.toString())
                .putMetadata("plan_id", plan.getId().toString());


        try {

            String stripeCustomerId=user.getStripeCustomerId();

            if(stripeCustomerId == null || stripeCustomerId.isEmpty()){
                params.setCustomerEmail(user.getUsername());
            }
            else{
                params.setCustomer(stripeCustomerId);
            }


            Session session = Session.create(params.build());//this is where we api call to the stripe
            return new CheckoutResponse(session.getUrl());
        } catch (StripeException e) {
            log.error("Stripe checkout session creation failed for userId={}, planId={}",
                    userId, plan.getId(), e);
            throw new RuntimeException("Unable to create checkout session", e);
        }
    }

    @Override
    public PortalResponse openCustomerPortal() {

        return null;
    }
}



