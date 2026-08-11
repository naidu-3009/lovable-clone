package com.projectlove.lovable_clone.Services;

import com.projectlove.lovable_clone.dto.projects.ProjectRequest;
import com.projectlove.lovable_clone.dto.projects.ProjectResponse;
import com.projectlove.lovable_clone.dto.projects.ProjectSummaryResponse;

import java.util.List;

public interface ProjectService {
    List<ProjectSummaryResponse> getUserProjects();
    ProjectResponse getUserProjectById(Long projectId);
    ProjectResponse createProject(ProjectRequest request);
    ProjectResponse updateProject(Long projectId, ProjectRequest request);
    void softDelete(Long projectId);
}
