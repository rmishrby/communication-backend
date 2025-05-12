package com.example.distribution.util;

import com.example.distribution.dto.*;
import com.example.distribution.entity.Project;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.Collectors;

public class ProjectMapper {

    private static final Logger logger = LoggerFactory.getLogger(ProjectMapper.class);

    public static Project toEntity(ProjectRequest request) {
        Project project = new Project();
        project.setTitle(request.getTitle());
        project.setDescription(request.getDescription());
        project.setCreatedBy(request.getCreatedBy());

        logger.debug("Mapped ProjectRequest to Project entity: title={}, createdBy={}", request.getTitle(), request.getCreatedBy());
        return project;
    }

    public static ProjectResponse toDto(Project project) {
        ProjectResponse response = new ProjectResponse();
        response.setId(project.getId());
        response.setTitle(project.getTitle());
        response.setDescription(project.getDescription());
        response.setCreatedBy(project.getCreatedBy());
        response.setCreatedAt(project.getCreatedAt());

        logger.debug("Mapped Project entity to ProjectResponse DTO: id={}", project.getId());
        return response;
    }

    public static ProjectSummaryResponse mapToSummary(Project project) {
        logger.debug("Mapping Project entity to ProjectSummaryResponse: id={}", project.getId());
        return new ProjectSummaryResponse(
                project.getId(),
                project.getTitle(),
                project.getDescription(),
                project.getCreatedBy(),
                project.getCreatedAt()
        );
    }

    public static ProjectDetailResponse mapToDetail(Project project) {
        List<ProjectUpdateResponse> updateResponses = project.getUpdates().stream()
                .map(ProjectUpdateMapper::mapToResponse)
                .collect(Collectors.toList());

        logger.debug("Mapping Project entity to ProjectDetailResponse: id={}, updatesCount={}", project.getId(), updateResponses.size());
        return new ProjectDetailResponse(
                project.getId(),
                project.getTitle(),
                project.getDescription(),
                project.getCreatedBy(),
                project.getCreatedAt(),
                updateResponses
        );
    }
}
