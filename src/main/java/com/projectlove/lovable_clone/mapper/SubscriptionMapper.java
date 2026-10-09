package com.projectlove.lovable_clone.mapper;


import com.projectlove.lovable_clone.dto.subscription.PlanResponse;
import com.projectlove.lovable_clone.dto.subscription.SubscriptionResponse;
import com.projectlove.lovable_clone.entity.Plan;
import com.projectlove.lovable_clone.entity.Subscription;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface SubscriptionMapper {

    @Mapping(source = "currentPeriodEnd",target = "periodEnd")
    SubscriptionResponse toSubscriptionResponse(Subscription subscription);

    PlanResponse toPlanResponse(Plan plan);

}
