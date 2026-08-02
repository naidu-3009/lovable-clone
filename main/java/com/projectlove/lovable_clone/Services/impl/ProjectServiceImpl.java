package com.projectlove.lovable_clone.Services.impl;

import com.projectlove.lovable_clone.Services.ProjectService;
import com.projectlove.lovable_clone.dto.projects.ProjectRequest;
import com.projectlove.lovable_clone.dto.projects.ProjectResponse;
import com.projectlove.lovable_clone.dto.projects.ProjectSummaryResponse;
import com.projectlove.lovable_clone.entity.Project;
import com.projectlove.lovable_clone.entity.User;
import com.projectlove.lovable_clone.repository.ProjectRepository;
import com.projectlove.lovable_clone.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class ProjectServiceImpl implements ProjectService {
    ProjectRepository projectRepository;
    UserRepository userRepository;

    @Override
    public List<ProjectSummaryResponse> getUserProjects(Long userId) {
        return List.of();
    }

    @Override
    public ProjectResponse getUserProjectById(Long id, Long userId) {
        return null;
    }

    @Override
    public ProjectResponse createProject(ProjectRequest request, Long userId) {
        User owner=userRepository.findById(userId).orElseThrow();
        Project project=Project.builder().name(request.name()).owner(owner).build();
        return null;
    }

    @Override
    public ProjectResponse updateProject(Long id, ProjectRequest request, Long userId) {
        return null;
    }

    @Override
    public void softDelete(Long id, Long userId) {

    }
}
