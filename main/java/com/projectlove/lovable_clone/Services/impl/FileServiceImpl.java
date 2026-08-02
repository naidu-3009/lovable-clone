package com.projectlove.lovable_clone.Services.impl;

import com.projectlove.lovable_clone.Services.FileService;
import com.projectlove.lovable_clone.dto.projects.FileContentResponse;
import com.projectlove.lovable_clone.dto.projects.FileNode;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class FileServiceImpl implements FileService {
    @Override
    public List<FileNode> getFileTree(Long userId, Long projectId) {
        return List.of();
    }

    @Override
    public FileContentResponse getFileContent(Long projectId, String path, Long userId) {
        return null;
    }
}
