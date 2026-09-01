package com.projectlove.lovable_clone.Services;

import com.projectlove.lovable_clone.dto.chat.ChatEventResponse;
import com.projectlove.lovable_clone.dto.chat.ChatResponse;

import java.util.List;

public interface ChatService {

    List<ChatResponse> getProjectChatHistory(Long projectId);


}
