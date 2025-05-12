package com.example.distribution.util;

import com.example.distribution.dto.ProjectUpdateResponse;
import com.example.distribution.entity.ProjectUpdate;

public class ProjectUpdateMapper {
    public static ProjectUpdateResponse mapToResponse(ProjectUpdate update) {
        ProjectUpdateResponse res = new ProjectUpdateResponse();
        res.setId(update.getId());
        res.setTitle(update.getTitle());
        res.setContent(update.getContent());
        res.setCreatedAt(update.getCreatedAt());
        res.setTaggedUsers(update.getTaggedUsers());

        if (update.getProject() != null) {
            res.setProjectId(update.getProject().getId());
            res.setProjectTitle(update.getProject().getTitle());
        }
        return res;
    }
}
