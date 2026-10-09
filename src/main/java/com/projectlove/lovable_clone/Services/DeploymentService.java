package com.projectlove.lovable_clone.Services;

import com.projectlove.lovable_clone.dto.deploy.DeployResponse;

public interface DeploymentService {
    DeployResponse deploy(Long projectId);

}
