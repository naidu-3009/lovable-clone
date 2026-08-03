package com.projectlove.lovable_clone.Services.impl;

import com.projectlove.lovable_clone.Services.ProjectService;
import com.projectlove.lovable_clone.dto.projects.ProjectRequest;
import com.projectlove.lovable_clone.dto.projects.ProjectResponse;
import com.projectlove.lovable_clone.dto.projects.ProjectSummaryResponse;
import com.projectlove.lovable_clone.entity.Project;
import com.projectlove.lovable_clone.entity.User;
import com.projectlove.lovable_clone.mapper.ProjectMapper;
import com.projectlove.lovable_clone.repository.ProjectRepository;
import com.projectlove.lovable_clone.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.time.Instant;
import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@Transactional
public class ProjectServiceImpl implements ProjectService {
    ProjectRepository projectRepository;
    UserRepository userRepository;
    ProjectMapper projectMapper;

    @Override
    public List<ProjectSummaryResponse> getUserProjects(Long userId) {
        return projectMapper.toListOfProjectSummaryResponse(projectRepository.findAllAccessibleByUser(userId));
    }

    @Override
    public ProjectResponse getUserProjectById(Long projectId, Long userId) {
        Project project= getUserProjectByIdInternal(projectId,userId);
        return projectMapper.toProjectResponse(project);


    }

    @Override
    public ProjectResponse createProject(ProjectRequest request, Long userId) {
        User owner=userRepository.findById(userId).orElseThrow();
        Project project=Project.builder().name(request.name()).owner(owner).isPublic(false).build();
        project = projectRepository.save(project);
        return projectMapper.toProjectResponse(project);
    }

    @Override
    public ProjectResponse updateProject(Long projectId, ProjectRequest request, Long userId) {
        Project project=getUserProjectByIdInternal(projectId,userId);

        if(!project.getOwner().getId().equals(userId)){
            throw new RuntimeException("You are not allowed to delete this project");
        }

        project.setName(request.name());
//        projectRepository.save(project);(we can ignore this line cuz if we add @transactional tag it dirtychecks and updates the db) but no harm in writing
        projectRepository.save(project);
        return projectMapper.toProjectResponse(project);
    }

    @Override
    public void softDelete(Long projectId, Long userId) {
        Project project=getUserProjectByIdInternal(projectId,userId);
        if(!project.getOwner().getId().equals(userId)){
            throw new RuntimeException("You are not allowed to delete this project");
        }
        project.setDeletedAt(Instant.now());
        projectRepository.save(project);
    }


    //internal use
    private Project getUserProjectByIdInternal(Long projectId,Long userId){
        Project project= projectRepository.findAllAccessibleByUserId(projectId,userId).orElseThrow();
        return project;
    }

}
