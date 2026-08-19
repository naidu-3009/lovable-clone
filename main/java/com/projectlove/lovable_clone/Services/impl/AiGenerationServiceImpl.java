package com.projectlove.lovable_clone.Services.impl;

import com.projectlove.lovable_clone.Services.AiGenerationService;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;


@Service
public class AiGenerationServiceImpl implements AiGenerationService {
    @Override
    public Flux<String> streamResponse(String message, Long aLong) {
        return null;
    }
}
