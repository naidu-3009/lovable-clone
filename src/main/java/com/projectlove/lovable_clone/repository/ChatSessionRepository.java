package com.projectlove.lovable_clone.repository;

import com.projectlove.lovable_clone.entity.ChatSession;
import com.projectlove.lovable_clone.entity.ChatSessionId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatSessionRepository extends JpaRepository<ChatSession, ChatSessionId> {


}
