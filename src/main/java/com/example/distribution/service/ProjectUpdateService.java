package com.example.distribution.service;


import com.example.distribution.dto.PagedProjectUpdateResponse;
import com.example.distribution.dto.ProjectUpdateRequest;
import com.example.distribution.dto.ProjectUpdateResponse;
import com.example.distribution.entity.ProjectUpdate;
import com.example.distribution.exception.ProjectUpdateNotFoundException;
import com.example.distribution.repository.ProjectUpdateRepository;
import com.example.distribution.util.ProjectUpdateMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
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

        return ProjectUpdateMapper.mapToResponse(saved);
    }


    public PagedProjectUpdateResponse getAllUpdates(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ProjectUpdate> updatePage = projectUpdateRepository.findAllByOrderByCreatedAtDesc(pageable);

        List<ProjectUpdateResponse> responses = updatePage.getContent()
                .stream()
                .map(ProjectUpdateMapper::mapToResponse)
                .collect(Collectors.toList());

        PagedProjectUpdateResponse result = new PagedProjectUpdateResponse();
        result.setUpdates(responses);
        result.setCurrentPage(updatePage.getNumber());
        result.setTotalPages(updatePage.getTotalPages());
        result.setTotalElements(updatePage.getTotalElements());

        return result;
    }

    public void addTaggedUsers(Long id, List<String> users) {
        ProjectUpdate update = projectUpdateRepository.findById(id)
                .orElseThrow(() -> new ProjectUpdateNotFoundException(id));

        Set<String> tags = update.getTaggedUsers();
        tags.addAll(users);
        update.setTaggedUsers(tags);
        projectUpdateRepository.save(update);
    }

    public void removeTaggedUsers(Long id, List<String> users) {
        ProjectUpdate update = projectUpdateRepository.findById(id)
                .orElseThrow(() -> new ProjectUpdateNotFoundException(id));

        Set<String> tags = update.getTaggedUsers();
        users.forEach(tags::remove);
        update.setTaggedUsers(tags);
        projectUpdateRepository.save(update);
    }
}

