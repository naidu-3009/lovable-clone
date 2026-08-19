package com.projectlove.lovable_clone.controllers;


import com.projectlove.lovable_clone.Services.AiGenerationService;
import com.projectlove.lovable_clone.chat.ChatRequest;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.awt.*;

@RestController
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true,level = AccessLevel.PRIVATE)
public class ChatController {
    AiGenerationService aiGenerationService;

    @PostMapping(value = "/api/chat/stream",produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ServerSentEvent<String >> streamChat(
            @RequestBody ChatRequest request
    ){
        return aiGenerationService.streamResponse(request.message(),request.projectId())
                .map(data-> ServerSentEvent.<String>builder()
                        .data(data)
                        .build());

    }
}

