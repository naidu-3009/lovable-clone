package com.projectlove.lovable_clone.dto.chat;

import com.projectlove.lovable_clone.enums.ChatEventType;
import jakarta.persistence.*;

public record ChatEventResponse(@Id
                                Long id,
                                ChatEventType chatEventType,
                                Integer sequenceOrder,
                                String content,
                                String metadata,
                                String filePath//null unless file edit
) {
}
