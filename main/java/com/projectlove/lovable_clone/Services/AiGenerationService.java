package com.projectlove.lovable_clone.Services;

import com.projectlove.lovable_clone.dto.chat.StreamResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service

public interface AiGenerationService {
    Flux<StreamResponse> streamResponse(String message, Long aLong);
}
