package com.projectlove.lovable_clone.Services;

import com.projectlove.lovable_clone.dto.member.InviteMemberRequest;
import com.projectlove.lovable_clone.dto.member.MemberResponse;
import com.projectlove.lovable_clone.dto.member.updateRoleRequest;

import java.util.List;

public interface ProjectMemberService {
       List<MemberResponse> getProjectMembers(Long projectId) ;

         MemberResponse inviteMember(Long projectId, InviteMemberRequest request);

    MemberResponse updateMemberRole(Long projectId, Long memberId, updateRoleRequest request);

    void removeProjectMember(Long projectId, Long memberId);
}
