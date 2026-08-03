package com.projectlove.lovable_clone.Services;

import com.projectlove.lovable_clone.dto.projects.ProjectRequest;
import com.projectlove.lovable_clone.dto.projects.ProjectResponse;
import com.projectlove.lovable_clone.dto.projects.ProjectSummaryResponse;

import java.util.List;

public interface ProjectService {
    List<ProjectSummaryResponse> getUserProjects(Long userId);
    ProjectResponse getUserProjectById(Long projectId,Long userId);
    ProjectResponse createProject(ProjectRequest request, Long userId);
    ProjectResponse updateProject(Long projectId, ProjectRequest request,Long userId);
    void softDelete(Long projectId, Long userId);
}
