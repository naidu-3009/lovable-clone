package com.projectlove.lovable_clone.mapper;

import com.projectlove.lovable_clone.dto.projects.ProjectResponse;
import com.projectlove.lovable_clone.dto.projects.ProjectSummaryResponse;
import com.projectlove.lovable_clone.entity.Project;
import com.projectlove.lovable_clone.enums.ProjectMemberRole;
import com.projectlove.lovable_clone.repository.ProjectRepository;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProjectMapper {
    ProjectResponse toProjectResponse(Project project);

    @Mapping(source = "project.id", target = "projectId")
    @Mapping(source = "project.name", target = "name")
    @Mapping(source = "project.createdAt", target = "createdAt")
    @Mapping(source = "project.updatedAt", target = "updatedAt")
    @Mapping(source = "role", target = "role")
    ProjectSummaryResponse toProjectSummaryResponse(Project project, ProjectMemberRole role);

    List<ProjectSummaryResponse> toListOfProjectSummaryResponse(List<Project> projects);


}