package com.projectlove.lovable_clone.Services.impl;

import com.projectlove.lovable_clone.Services.ProjectMemberService;
import com.projectlove.lovable_clone.dto.member.InviteMemberRequest;
import com.projectlove.lovable_clone.dto.member.MemberResponse;
import com.projectlove.lovable_clone.dto.member.updateRoleRequest;
//import com.projectlove.lovable_clone.entity.*;
import com.projectlove.lovable_clone.entity.Project;
import com.projectlove.lovable_clone.entity.ProjectMember;
import com.projectlove.lovable_clone.entity.ProjectMemberId;
import com.projectlove.lovable_clone.entity.User;
import com.projectlove.lovable_clone.mapper.ProjectMemberMapper;
import com.projectlove.lovable_clone.repository.ProjectMemberRepository;
import com.projectlove.lovable_clone.repository.ProjectRepository;
import com.projectlove.lovable_clone.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;


@Service
@FieldDefaults(makeFinal = true,level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
@Transactional
public class ProjectMemberServiceImpl implements ProjectMemberService {
    ProjectMemberRepository projectMemberRepository;
    ProjectRepository projectRepository;
    ProjectMemberMapper projectMemberMapper;
    UserRepository userRepository;

    @Override
    public List<MemberResponse> getProjectMembers(Long projectId, Long userId) {
        Project project=getUserProjectByIdInternal(projectId,userId);
        return projectMemberRepository.findByProjectId(projectId).stream().map(projectMemberMapper::toMemberResponseFromMember).toList();
    }


    @Override
    public MemberResponse inviteMember(Long projectId, InviteMemberRequest request, Long userId) throws RuntimeException{
        Project project = getUserProjectByIdInternal(projectId,userId);

        User invitee=userRepository.findByUsername(request.username()).orElseThrow();

        if(invitee.getId().equals(userId))
            throw new RuntimeException("you are not allowed to invite yourself");

        ProjectMemberId projectMemberId=new ProjectMemberId(projectId,userId);
        if(projectMemberRepository.existsById(projectMemberId))
            throw new RuntimeException("you are not allowed to invite multiple times");

        ProjectMember member=ProjectMember.builder().projectMemberId(projectMemberId).projectMemberRole(request.role()).user(invitee).invitedAt(Instant.now()).project(project).build();

        projectMemberRepository.save(member);
        return projectMemberMapper.toMemberResponseFromMember(member);

    }

    @Override
    public MemberResponse updateMemberRole(Long projectId, Long memberId, updateRoleRequest request, Long userId) {
        Project project=getUserProjectByIdInternal(projectId,userId);

        ProjectMemberId projectMemberId=new ProjectMemberId(projectId,memberId);
        ProjectMember projectMember=projectMemberRepository.findById(projectMemberId).orElseThrow();

        projectMember.setProjectMemberRole(request.role());
        projectMemberRepository.save(projectMember);
       return projectMemberMapper.toMemberResponseFromMember(projectMember);
    }

    @Override
    public void removeProjectMember(Long projectId, Long memberId, Long userId) {
        Project project=getUserProjectByIdInternal(projectId,userId);


        ProjectMemberId projectMemberId=new ProjectMemberId(projectId,memberId);

        if(!(projectMemberRepository.existsById(projectMemberId))){
            throw new RuntimeException("the member doesnt even exits,hence cant delete");
        }
        projectMemberRepository.deleteById(projectMemberId);
        return ;
    }

    //internal use
    private Project getUserProjectByIdInternal(Long projectId, Long userId){

        Project project= projectRepository.findAllAccessibleByUserId(projectId,userId).orElseThrow();
        return project;
    }

}
