package com.example.distribution.util;

import com.example.distribution.dto.ProjectUpdateResponse;
import com.example.distribution.entity.ProjectUpdate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProjectUpdateMapper {

    private static final Logger logger = LoggerFactory.getLogger(ProjectUpdateMapper.class);

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
            logger.debug("Mapped ProjectUpdate with project: projectId={}, projectTitle={}", update.getProject().getId(), update.getProject().getTitle());
        } else {
            logger.debug("Mapped ProjectUpdate with no associated project.");
        }

        logger.debug("Mapped ProjectUpdateResponse: updateId={}, title={}", update.getId(), update.getTitle());
        return res;
    }
}
