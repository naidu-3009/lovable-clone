package com.projectlove.lovable_clone.dto.projects;

import com.projectlove.lovable_clone.dto.auth.UserProfileResponse;

import java.time.Instant;

public record ProjectResponse(Long id,
                              String name,
                              Instant createdAt,
                              Instant updatedAt,
                              UserProfileResponse owner) {
}
