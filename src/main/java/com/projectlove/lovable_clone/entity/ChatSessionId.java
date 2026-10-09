package com.projectlove.lovable_clone.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;


@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ChatSessionId implements Serializable {
    Long projectId;
    Long userId;
}
