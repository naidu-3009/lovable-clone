package com.projectlove.lovable_clone.dto.projects;

import java.time.Instant;

public record FileNode(String path
, String type, Instant modifiedAt,Long size) {
}
