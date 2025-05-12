package com.example.distribution.service;

import com.example.distribution.dto.ProjectDetailResponse;
import com.example.distribution.dto.ProjectRequest;
import com.example.distribution.dto.ProjectResponse;
import com.example.distribution.dto.ProjectSummaryResponse;
import com.example.distribution.entity.Project;
import com.example.distribution.exception.NotFoundException;
import com.example.distribution.repository.ProjectRepository;
import com.example.distribution.util.ProjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProjectService {

    @Autowired
    private ProjectRepository projectRepository;

    public ProjectResponse createProject(ProjectRequest request) {
        Project project = ProjectMapper.toEntity(request);
        Project saved = projectRepository.save(project);
        return ProjectMapper.toDto(saved);
    }

    public List<ProjectSummaryResponse> getAllProjectsSummary() {
        return projectRepository.findAll()
                .stream()
                .map(ProjectMapper::mapToSummary)
                .collect(Collectors.toList());
    }

    public ProjectDetailResponse getProjectById(Long id) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Project not found with ID: ", id));

        return ProjectMapper.mapToDetail(project);
    }
}
