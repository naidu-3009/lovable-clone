package com.projectlove.lovable_clone.controllers;


import com.projectlove.lovable_clone.Services.UsageService;
import com.projectlove.lovable_clone.dto.subscription.PlanLimitResponse;
import com.projectlove.lovable_clone.dto.subscription.UsageTodayResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/usage")
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true,level = AccessLevel.PRIVATE)
public class UsageController {

     UsageService usageService;

    @GetMapping("/today")
    public ResponseEntity<UsageTodayResponse> getTodayUsage(){
        Long userId=1L;
        return ResponseEntity.ok(usageService.getTodayUsageOfUser(userId));
    }


    @GetMapping("/limits")
    public ResponseEntity<PlanLimitResponse> getPlanLimits(){
        Long userId=1L;
        return ResponseEntity.ok(usageService.getCurrentSubscriptionLimits(userId));
    }
}
