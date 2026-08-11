package com.projectlove.lovable_clone.Services.impl;

import com.projectlove.lovable_clone.Services.ProjectService;
import com.projectlove.lovable_clone.dto.projects.ProjectRequest;
import com.projectlove.lovable_clone.dto.projects.ProjectResponse;
import com.projectlove.lovable_clone.dto.projects.ProjectSummaryResponse;
import com.projectlove.lovable_clone.entity.Project;
import com.projectlove.lovable_clone.entity.ProjectMember;
import com.projectlove.lovable_clone.entity.ProjectMemberId;
import com.projectlove.lovable_clone.entity.User;
import com.projectlove.lovable_clone.enums.ProjectMemberRole;
import com.projectlove.lovable_clone.error.ResourceNotFoundException;
import com.projectlove.lovable_clone.mapper.ProjectMapper;
import com.projectlove.lovable_clone.repository.ProjectMemberRepository;
import com.projectlove.lovable_clone.repository.ProjectRepository;
import com.projectlove.lovable_clone.repository.UserRepository;
import com.projectlove.lovable_clone.security.AuthUtil;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.time.Instant;
import java.util.List;


@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@Transactional
public class ProjectServiceImpl implements ProjectService {
    ProjectRepository projectRepository;
    UserRepository userRepository;
    ProjectMapper projectMapper;
    ProjectMemberRepository projectMemberRepository;
    AuthUtil authUtil;

    @Override
    public List<ProjectSummaryResponse> getUserProjects() {
        Long userId= authUtil.getCurrentUserId();
        return projectMapper.toListOfProjectSummaryResponse(projectRepository.findAllAccessibleByUser(userId));
    }

    @Override
    public ProjectResponse getUserProjectById(Long projectId) {
        Long userId= authUtil.getCurrentUserId();
        Project project= getUserProjectByIdInternal(projectId);
        return projectMapper.toProjectResponse(project);


    }

    @Override
    public ProjectResponse createProject(ProjectRequest request) {
        Long userId= authUtil.getCurrentUserId();
        User owner=userRepository.findById(userId).orElseThrow(
                ()->new ResourceNotFoundException("user id not found",userId.toString())
        );
        Project project=Project.builder().name(request.name()).isPublic(false).build();
        project = projectRepository.save(project);
        ProjectMemberId projectMemberId=new ProjectMemberId(project.getId(),userId);

             ProjectMember projectMember=ProjectMember.builder().projectMemberRole(ProjectMemberRole.OWNER).user(owner).acceptedAt(Instant.now()).invitedAt(Instant.now()).projectMemberId(projectMemberId).project(project).build();
         projectMemberRepository.save(projectMember);
        return projectMapper.toProjectResponse(project);

    }

    @Override
    public ProjectResponse updateProject(Long projectId, ProjectRequest request) {
        Long userId= authUtil.getCurrentUserId();
        Project project=getUserProjectByIdInternal(projectId);

        project.setName(request.name());
//        projectRepository.save(project);(we can ignore this line cuz if we add @transactional tag it dirtychecks and updates the db) but no harm in writing
        projectRepository.save(project);
        return projectMapper.toProjectResponse(project);
    }

    @Override
    public void softDelete(Long projectId) {
        Long userId= authUtil.getCurrentUserId();
        Project project=getUserProjectByIdInternal(projectId);

        project.setDeletedAt(Instant.now());
        projectRepository.save(project);
    }


    //internal use
    private Project getUserProjectByIdInternal(Long projectId){
        Long userId= authUtil.getCurrentUserId();
        Project project= projectRepository.findAllAccessibleByUserId(projectId,userId).orElseThrow(()->{return new ResourceNotFoundException("Project",projectId.toString());
        });
        return project;
    }

}
