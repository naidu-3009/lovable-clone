package com.projectlove.lovable_clone.Services.impl;

import com.projectlove.lovable_clone.Services.ProjectFileService;
import com.projectlove.lovable_clone.dto.projects.FileContentResponse;
import com.projectlove.lovable_clone.dto.projects.FileNode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;


@Slf4j
@Service
public class ProjectFileServiceImpl implements ProjectFileService {
    @Override
    public List<FileNode> getFileTree(Long userId, Long projectId) {
        return List.of();
    }

    @Override
    public FileContentResponse getFileContent(Long projectId, String path, Long userId) {
        return null;
    }

    @Override
    public void saveFile(Long projectId, String filePath, String fileContent) {
        log.info("saving the filepath{}",filePath);
        //we are saving our file metadata in our postgresdb and upload our file content to min io

    }
}
