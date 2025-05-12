package com.example.distribution.util;

import com.example.distribution.dto.*;
import com.example.distribution.entity.Project;

import java.util.List;
import java.util.stream.Collectors;

public class ProjectMapper {

    public static Project toEntity(ProjectRequest request) {
        Project project = new Project();
        project.setTitle(request.getTitle());
        project.setDescription(request.getDescription());
        project.setCreatedBy(request.getCreatedBy());
        return project;
    }

    public static ProjectResponse toDto(Project project) {
        ProjectResponse response = new ProjectResponse();
        response.setId(project.getId());
        response.setTitle(project.getTitle());
        response.setDescription(project.getDescription());
        response.setCreatedBy(project.getCreatedBy());
        response.setCreatedAt(project.getCreatedAt());
        return response;
    }

    public static ProjectSummaryResponse mapToSummary(Project project) {
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

        return new ProjectDetailResponse(
                project.getId(),
                project.getTitle(),
                project.getDescription(),
                project.getCreatedBy(),
                project.getCreatedAt(),
                updateResponses
        );
    };

}
