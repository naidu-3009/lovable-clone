package com.projectlove.lovable_clone.Services.impl;

import com.projectlove.lovable_clone.Services.ChatService;
import com.projectlove.lovable_clone.dto.chat.ChatResponse;
import com.projectlove.lovable_clone.entity.ChatMessage;
import com.projectlove.lovable_clone.entity.ChatSession;
import com.projectlove.lovable_clone.entity.ChatSessionId;
import com.projectlove.lovable_clone.mapper.ChatMapper;
import com.projectlove.lovable_clone.repository.ChatMessageRepository;
import com.projectlove.lovable_clone.repository.ChatSessionRepository;
import com.projectlove.lovable_clone.security.AuthUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
@Slf4j
public class ChatServiceImpl implements ChatService {

    private final ChatMessageRepository chatMessageRepository;
    private final AuthUtil authUtil;
    private final ChatSessionRepository chatSessionRepository;
    private final ChatMapper chatMapper;

    @Override
    public List<ChatResponse> getProjectChatHistory(Long projectId) {
        Long userId=authUtil.getCurrentUserId();
        ChatSession chatSession=chatSessionRepository.getReferenceById(new ChatSessionId(projectId,userId));
        List<ChatMessage> chatMessages=chatMessageRepository.findByChatSession(chatSession);
        return chatMapper.toChatResponse(chatMessages);
    }
}
