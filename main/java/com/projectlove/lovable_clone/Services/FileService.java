package com.projectlove.lovable_clone.Services;

import com.projectlove.lovable_clone.dto.projects.FileContentResponse;
import com.projectlove.lovable_clone.dto.projects.FileNode;

import java.util.List;

public interface FileService {
         List<FileNode> getFileTree(Long userId, Long projectId);

    FileContentResponse getFileContent(Long projectId, String path, Long userId);
}
