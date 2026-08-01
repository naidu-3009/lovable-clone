package com.projectlove.lovable_clone.Services;

import com.projectlove.lovable_clone.dto.member.InviteMemberRequest;
import com.projectlove.lovable_clone.dto.member.MemberResponse;
import com.projectlove.lovable_clone.dto.member.updateRoleRequest;
import com.projectlove.lovable_clone.entity.ProjectMember;
import org.jspecify.annotations.Nullable;

import java.util.List;

public interface ProjectMemberService {
       List<ProjectMember> getProjectMembers(Long projectId, Long userId) ;

         MemberResponse inviteMember(Long projectId, InviteMemberRequest request, Long userId);

    MemberResponse updateMemberRole(Long projectId, Long memberId, updateRoleRequest request, Long userId);

    MemberResponse deleteProjectMember(Long projectId, Long memberId, Long userId);
}
