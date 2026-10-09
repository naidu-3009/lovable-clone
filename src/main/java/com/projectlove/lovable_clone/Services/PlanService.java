package com.projectlove.lovable_clone.Services;

import com.projectlove.lovable_clone.dto.subscription.PlanResponse;
import org.jspecify.annotations.Nullable;

import java.util.List;

public interface PlanService {
  List<PlanResponse> getAllActivePlans();
}
