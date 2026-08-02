package com.projectlove.lovable_clone.Services.impl;

import com.projectlove.lovable_clone.Services.ProjectMemberService;
import com.projectlove.lovable_clone.dto.member.InviteMemberRequest;
import com.projectlove.lovable_clone.dto.member.MemberResponse;
import com.projectlove.lovable_clone.dto.member.updateRoleRequest;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class ProjectMemberServiceImpl implements ProjectMemberService {
    @Override
    public List<MemberResponse> getProjectMembers(Long projectId, Long userId) {
        return List.of();
    }

    @Override
    public MemberResponse inviteMember(Long projectId, InviteMemberRequest request, Long userId) {
        return null;
    }

    @Override
    public MemberResponse updateMemberRole(Long projectId, Long memberId, updateRoleRequest request, Long userId) {
        return null;
    }

    @Override
    public MemberResponse deleteProjectMember(Long projectId, Long memberId, Long userId) {
        return null;
    }
}
