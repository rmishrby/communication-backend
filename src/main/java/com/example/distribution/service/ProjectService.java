package com.example.distribution.service;

import com.example.distribution.dto.ProjectDetailResponse;
import com.example.distribution.dto.ProjectRequest;
import com.example.distribution.dto.ProjectResponse;
import com.example.distribution.dto.ProjectSummaryResponse;
import com.example.distribution.entity.Project;
import com.example.distribution.exception.NotFoundException;
import com.example.distribution.repository.ProjectRepository;
import com.example.distribution.util.ProjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProjectService {

    private static final Logger logger = LoggerFactory.getLogger(ProjectService.class);

    @Autowired
    private ProjectRepository projectRepository;

    public ProjectResponse createProject(ProjectRequest request) {
        logger.info("Creating new project: {}", request.getTitle());
        Project project = ProjectMapper.toEntity(request);
        Project saved = projectRepository.save(project);
        logger.info("Project saved with ID: {}", saved.getId());
        return ProjectMapper.toDto(saved);
    }

    public List<ProjectSummaryResponse> getAllProjectsSummary() {
        logger.info("Fetching all project summaries");
        List<ProjectSummaryResponse> summaries = projectRepository.findAll()
                .stream()
                .map(ProjectMapper::mapToSummary)
                .collect(Collectors.toList());
        logger.info("Total projects found: {}", summaries.size());
        return summaries;
    }

    public ProjectDetailResponse getProjectById(Long id) {
        logger.info("Fetching project details for ID: {}", id);
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> {
                    logger.warn("Project not found with ID: {}", id);
                    return new NotFoundException("Project not found with ID: ", id);
                });

        logger.info("Project found: {}", project.getTitle());
        return ProjectMapper.mapToDetail(project);
    }
}
