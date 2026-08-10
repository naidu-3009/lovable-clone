package com.projectlove.lovable_clone.dto.member;

import com.projectlove.lovable_clone.enums.ProjectMemberRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

public record InviteMemberRequest (
        @Email String username,
        @NotNull ProjectMemberRole role){}
