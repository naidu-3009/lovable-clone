package com.projectlove.lovable_clone.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.time.Instant;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
public class ProjectFile {

    Long id;
    Project project;
    String path;
    String minioObjectKey;

    User createdBy;
    User updatedBy;


    Instant createdAt;
    Instant updatedAt;
    Instant deletedAt;

}
