package com.projectlove.lovable_clone.entity;


import com.projectlove.lovable_clone.enums.MessageRole;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.time.Instant;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
public class ChatMessage {

    Long id;

    ChatSession chatSession;

    String content;

    String toolCalls;

    Integer tokensUsed;

    Instant createdAt;

    MessageRole role;



}
