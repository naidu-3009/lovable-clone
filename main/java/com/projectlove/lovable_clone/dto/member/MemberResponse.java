package com.projectlove.lovable_clone.dto.member;

import com.projectlove.lovable_clone.enums.ProjectMemberRole;

public record MemberResponse(
        Long userId,
        String username,
        String name,
        String avatarUrl,
        ProjectMemberRole role,
        String projectId
) {
}
