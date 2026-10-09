package com.projectlove.lovable_clone.security;

import com.projectlove.lovable_clone.enums.ProjectMemberRole;
import com.projectlove.lovable_clone.enums.ProjectPerimission;
import com.projectlove.lovable_clone.repository.ProjectMemberRepository;
import com.projectlove.lovable_clone.repository.ProjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Component;

@Component("security")
@RequiredArgsConstructor
public class SecurtiyExpressions {

    private final ProjectMemberRepository projectMemberRepository;
    private final AuthUtil authUtil;


    public boolean hasPermission(Long projectId,ProjectPerimission projectPerimission){
        Long userId=authUtil.getCurrentUserId();
        return projectMemberRepository.findRoleByProjectIdAndUserId(projectId,userId).map(role -> role.getPermissions().contains(projectPerimission)).orElse(false);
    }

    public boolean canViewProject(Long projectId){
        return hasPermission(projectId, ProjectPerimission.VIEW);
    }


    public boolean canEditProject(Long projectId){
        return hasPermission(projectId, ProjectPerimission.EDIT);
    }


    public boolean canDeleteProject(Long projectId){
        return hasPermission(projectId, ProjectPerimission.DELETE);
    }

    public boolean canViewMembers(Long projectId){
        return hasPermission(projectId, ProjectPerimission.VIEW_MEMBERS);
    }

    public boolean canManageMembers(Long projectId){
        return hasPermission(projectId, ProjectPerimission.MANAGE_MEMBERS);
    }





}
