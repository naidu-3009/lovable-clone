package com.projectlove.lovable_clone.entity;


import com.projectlove.lovable_clone.enums.SubscriptionStatus;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.time.Instant;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
public class Subscription {

    User user;
    Plan plan;
    Long id;
    Long userId;
    Long planId;
    SubscriptionStatus status;
    Instant currentPeriodStart;
    Instant currentPeriodEnd;
    Instant createdAt;
    Instant updatedAt;
    Boolean cancelAtPeriodEnd=false;

}
