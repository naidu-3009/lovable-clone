package com.projectlove.lovable_clone.Services.impl;

import com.projectlove.lovable_clone.Services.ProjectFileService;
import com.projectlove.lovable_clone.dto.projects.FileContentResponse;
import com.projectlove.lovable_clone.dto.projects.FileNode;
import com.projectlove.lovable_clone.entity.Project;
import com.projectlove.lovable_clone.entity.ProjectFile;
import com.projectlove.lovable_clone.error.ResourceNotFoundException;
import com.projectlove.lovable_clone.repository.ProjectFileRepository;
import com.projectlove.lovable_clone.repository.ProjectRepository;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.List;


@Slf4j
@Service
@RequiredArgsConstructor
public class ProjectFileServiceImpl implements ProjectFileService {

    private final ProjectRepository projectRepository;
    private final ProjectFileRepository projectFileRepository;
    private final MinioClient minioClient;

    @Value("${minio.project-bucket}")
    private String projectBucket;

    @Override
    public List<FileNode> getFileTree(Long userId, Long projectId) {

        List<ProjectFile>  projectFileList=projectFileRepository.findByProjectId(projectId);





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
        Project project=projectRepository.findById(projectId).orElseThrow(
                ()-> new ResourceNotFoundException("Project",projectId.toString())
        );


        String cleanPath=filePath.startsWith("/") ? filePath.substring(1):filePath;
        String objectKey=projectId + "/" + cleanPath;


        try {
            byte[] contentBytes = fileContent.getBytes(StandardCharsets.UTF_8);
            InputStream inputStream = new ByteArrayInputStream(contentBytes);
            // saving the file content
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(projectBucket)
                            .object(objectKey)
                            .stream(inputStream, contentBytes.length, -1)
                            .contentType(determineContentType(filePath))
                            .build());

            // Saving the metaData
            ProjectFile file = projectFileRepository.findByProjectIdAndPath(projectId, cleanPath)
                    .orElseGet(() -> ProjectFile.builder()
                            .project(project)
                            .path(cleanPath)
                            .minioObjectKey(objectKey) // Use the key we generated
                            .createdAt(Instant.now())
                            .build());

            file.setUpdatedAt(Instant.now());
            projectFileRepository.save(file);
            log.info("Saved file: {}", objectKey);
        } catch (Exception e) {
            log.error("Failed to save file {}/{}", projectId, cleanPath, e);
            throw new RuntimeException("File save failed", e);
        }


    }


    private String determineContentType(String path) {
        String type = URLConnection.guessContentTypeFromName(path);
        if (type != null) return type;
        if (path.endsWith(".jsx") || path.endsWith(".ts") || path.endsWith(".tsx")) return "text/javascript";
        if (path.endsWith(".json")) return "application/json";
        if (path.endsWith(".css")) return "text/css";

        return "text/plain";
    }


}
