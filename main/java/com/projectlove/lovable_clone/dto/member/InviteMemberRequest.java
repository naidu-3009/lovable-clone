package com.projectlove.lovable_clone.dto.member;

import com.projectlove.lovable_clone.enums.ProjectMemberRole;

public record InviteMemberRequest (String email, ProjectMemberRole role){}
