package com.projectlove.lovable_clone.dto.projects;

import jakarta.validation.constraints.NotBlank;

public record ProjectRequest(
        @NotBlank String name
) {
}
