package com.projectlove.lovable_clone.Services.impl;

import com.projectlove.lovable_clone.Services.SubscriptionService;
import com.projectlove.lovable_clone.dto.auth.UserProfileResponse;
import com.projectlove.lovable_clone.dto.subscription.CheckoutRequest;
import com.projectlove.lovable_clone.dto.subscription.CheckoutResponse;
import com.projectlove.lovable_clone.dto.subscription.PortalResponse;
import com.projectlove.lovable_clone.dto.subscription.SubscriptionResponse;
import com.projectlove.lovable_clone.entity.Plan;
import com.projectlove.lovable_clone.entity.Subscription;
import com.projectlove.lovable_clone.entity.User;
import com.projectlove.lovable_clone.enums.SubscriptionStatus;
import com.projectlove.lovable_clone.error.ResourceNotFoundException;
import com.projectlove.lovable_clone.mapper.SubscriptionMapper;
import com.projectlove.lovable_clone.repository.PlanRepository;
import com.projectlove.lovable_clone.repository.SubscriptionRespository;
import com.projectlove.lovable_clone.repository.UserRepository;
import com.projectlove.lovable_clone.security.AuthUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class SubscriptionServiceImpl implements SubscriptionService {

    private final AuthUtil authUtil;
    private final SubscriptionRespository subscriptionRespository;
    private final SubscriptionMapper subscriptionMapper;
    private final UserRepository userRepository;
    private final PlanRepository planRepository;




    @Override
    public SubscriptionResponse getCurrentSubscription() {
        Long userId=authUtil.getCurrentUserId();



        var currentSubscription= subscriptionRespository.findByUserIdAndStatusIn(userId, Set.of(
                SubscriptionStatus.ACTIVE,SubscriptionStatus.PAST_DUE,SubscriptionStatus.TRAILING
        )).orElse(
                new Subscription()
        );

        return subscriptionMapper.toSubscriptionResponse(currentSubscription);
    }

    @Override
    public void activateSubscription(Long userId, Long planId, String subscriptionId, String customerId) {
        boolean exists=subscriptionRespository.existsByStripeSubscriptionId(subscriptionId);

        if(exists) return;

        User user=getUser(userId);
        Plan plan=getPlan(planId);

        Subscription subscription=Subscription.builder().user(user).plan(plan).stripeSubscriptionId(subscriptionId).status(SubscriptionStatus.INCOMPLETE).build();

        subscriptionRespository.save(subscription);
    }

    @Override
    public void updateSubscription(String subscriptionId, SubscriptionStatus status, Instant periodStart, Instant periodEnd, Boolean cancelAtPeriodEnd, Long planId) {

    }

    @Override
    public void cancelSubscription(String subscriptionId) {

    }

    @Override
    public void renewSubscriptionPeriod(String gatewaySubscriptionId, Instant periodStart, Instant periodEnd) {
            Subscription subscription=getSubscription(gatewaySubscriptionId);

            Instant newStart=periodStart !=null ?periodStart : subscription.getCurrentPeriodEnd();
            subscription.setCurrentPeriodStart(newStart);
            subscription.setCurrentPeriodEnd(periodEnd);

            if(subscription.getStatus()== SubscriptionStatus.PAST_DUE || subscription.getStatus()==SubscriptionStatus.INCOMPLETE){
                subscription.setStatus(SubscriptionStatus.ACTIVE);
            }

            subscriptionRespository.save(subscription);


    }

    @Override
    public void markSubscriptionPastDue(String subscriptionId) {

    }



    //Internal Methods

    private User getUser(Long userId){
        return userRepository.findById(userId).orElseThrow(()->new ResourceNotFoundException("User",userId.toString()));
    }

    private Plan getPlan(Long planId){
        return planRepository.findById(planId).orElseThrow(()->new ResourceNotFoundException("Plan",planId.toString()));
    }


    private Subscription getSubscription(String gatewaySubscriptionId) {
        return subscriptionRespository.findByStripeSubscriptionId(gatewaySubscriptionId).orElseThrow(() -> new ResourceNotFoundException("Subscription", gatewaySubscriptionId));
    }
}
