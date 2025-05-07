package com.example.distribution.util;

import com.example.distribution.dto.ProjectUpdateResponse;
import com.example.distribution.entity.ProjectUpdate;

public class ProjectUpdateMapper {
    public static ProjectUpdateResponse mapToResponse(ProjectUpdate projectUpdate) {
        if (projectUpdate == null) {
            return null;
        }
        ProjectUpdateResponse response = new ProjectUpdateResponse();
        response.setId(projectUpdate.getId());
        response.setTitle(projectUpdate.getTitle());
        response.setContent(projectUpdate.getContent());
        response.setCreatedAt(projectUpdate.getCreatedAt());
        response.setTaggedUsers(projectUpdate.getTaggedUsers());
        return response;
    }
}
