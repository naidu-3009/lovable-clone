package com.projectlove.lovable_clone.dto.chat;

import com.projectlove.lovable_clone.entity.ChatEvent;
import com.projectlove.lovable_clone.entity.ChatSession;
import com.projectlove.lovable_clone.enums.MessageRole;

import java.time.Instant;
import java.util.List;




public record ChatResponse (
    Long id,
    ChatSession chatSession,
    String content,
    Integer tokensUsed,
    Instant createdAt,
    MessageRole role,
    List<ChatEvent> events
    ){}
