package com.projectlove.lovable_clone.mapper;


import com.projectlove.lovable_clone.dto.chat.ChatResponse;
import com.projectlove.lovable_clone.entity.ChatMessage;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ChatMapper {

    List<ChatResponse> toChatResponse(List<ChatMessage> chatMessages);
}
