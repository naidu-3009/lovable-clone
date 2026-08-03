package com.projectlove.lovable_clone.mapper;


import com.projectlove.lovable_clone.dto.projects.ProjectResponse;
import com.projectlove.lovable_clone.dto.projects.ProjectSummaryResponse;
import com.projectlove.lovable_clone.entity.Project;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface ProjectMapper {
    ProjectResponse toProjectResponse(Project project);
    List<ProjectSummaryResponse> toListOfProjectSummaryResponse(List<Project> projects);
}
