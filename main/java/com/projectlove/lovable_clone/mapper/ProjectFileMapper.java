package com.projectlove.lovable_clone.mapper;


import com.projectlove.lovable_clone.dto.projects.FileNode;
import com.projectlove.lovable_clone.entity.ProjectFile;
import org.mapstruct.Mapper;
import org.mapstruct.MapperConfig;

import java.io.File;
import java.util.List;

@Mapper(componentModel = "spring")
public interface ProjectFileMapper {

    public List<FileNode > toFileNode(List<ProjectFile> projectFileList);


}
