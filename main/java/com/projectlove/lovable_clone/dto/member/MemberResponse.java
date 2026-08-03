package com.projectlove.lovable_clone.dto.member;

import com.projectlove.lovable_clone.enums.ProjectMemberRole;

public record MemberResponse(
        Long userId,
        String email,
        String name,
        String avatarUrl,
        ProjectMemberRole projectMemberRole,
        String projectId
) {
}
