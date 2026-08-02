package com.projectlove.lovable_clone.Services.impl;

import com.projectlove.lovable_clone.Services.PlanService;
import com.projectlove.lovable_clone.dto.subscription.PlanResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlanServiceImpl implements PlanService {
    @Override
    public List<PlanResponse> getAllActivePlans() {
        return List.of();
    }
}
