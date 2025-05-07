package com.example.distribution.service;


import com.example.distribution.dto.ProjectUpdateRequest;
import com.example.distribution.dto.ProjectUpdateResponse;
import com.example.distribution.entity.ProjectUpdate;
import com.example.distribution.repository.ProjectUpdateRepository;
import com.example.distribution.util.ProjectUpdateMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProjectUpdateService {

    @Autowired
    private ProjectUpdateRepository projectUpdateRepository;
    @Autowired
    private NotificationService notificationService;


    public ProjectUpdateResponse createUpdate(ProjectUpdateRequest request) {
        ProjectUpdate update = new ProjectUpdate();
        update.setTitle(request.getTitle());
        update.setContent(request.getContent());
        update.setTaggedUsers(request.getTaggedUsers());

        ProjectUpdate saved = projectUpdateRepository.save(update);
        notificationService.notifyTaggedUsers(saved);

        return ProjectUpdateMapper.mapToResponse(saved);
    }

    public List<ProjectUpdateResponse> getAllUpdates() {
        return projectUpdateRepository.findAllByOrderByCreatedAtDesc()
                .stream()
                .map(ProjectUpdateMapper::mapToResponse)
                .collect(Collectors.toList());
    }
}

