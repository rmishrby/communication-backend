package com.example.distribution.service;

import com.example.distribution.dto.PagedProjectUpdateResponse;
import com.example.distribution.dto.ProjectUpdateRequest;
import com.example.distribution.dto.ProjectUpdateResponse;
import com.example.distribution.entity.Project;
import com.example.distribution.entity.ProjectUpdate;
import com.example.distribution.entity.User;
import com.example.distribution.exception.NotFoundException;
import com.example.distribution.repository.ProjectRepository;
import com.example.distribution.repository.ProjectUpdateRepository;
import com.example.distribution.repository.UserRepository;
import com.example.distribution.util.ProjectUpdateMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger logger = LoggerFactory.getLogger(ProjectUpdateService.class);

    @Autowired
    private ProjectUpdateRepository projectUpdateRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailSenderService emailSenderService;

    @Autowired
    private ProjectRepository projectRepository;

    public ProjectUpdateResponse createUpdate(Long projectId, ProjectUpdateRequest request) {
        logger.info("Creating update for project ID: {}", projectId);

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> {
                    logger.warn("Project not found with ID: {}", projectId);
                    return new NotFoundException("Project not found", projectId);
                });

        ProjectUpdate update = new ProjectUpdate();
        update.setTitle(request.getTitle());
        update.setContent(request.getContent());
        update.setTaggedUsers(request.getTaggedUsers());
        update.setProject(project);

        ProjectUpdate saved = projectUpdateRepository.save(update);
        logger.info("Project update saved with ID: {}", saved.getId());

        List<User> taggedUsers = userRepository.findByUsernameIn(request.getTaggedUsers());
        logger.info("Sending email notifications to {} tagged users", taggedUsers.size());
        emailSenderService.sendBulkEmails(taggedUsers, saved.getTitle(), saved.getContent());

        return ProjectUpdateMapper.mapToResponse(saved);
    }

    public PagedProjectUpdateResponse getAllUpdates(int page, int size) {
        logger.info("Fetching all updates - page: {}, size: {}", page, size);
        Pageable pageable = PageRequest.of(page, size);
        Page<ProjectUpdate> updatePage = projectUpdateRepository.findAllByOrderByCreatedAtDesc(pageable);

        List<ProjectUpdateResponse> responses = updatePage.getContent()
                .stream()
                .map(ProjectUpdateMapper::mapToResponse)
                .collect(Collectors.toList());

        logger.info("Total updates found: {}", updatePage.getTotalElements());

        PagedProjectUpdateResponse result = new PagedProjectUpdateResponse();
        result.setUpdates(responses);
        result.setCurrentPage(updatePage.getNumber());
        result.setTotalPages(updatePage.getTotalPages());
        result.setTotalElements(updatePage.getTotalElements());

        return result;
    }

    public void addTaggedUsers(Long id, List<String> users) {
        logger.info("Adding tagged users to update ID: {} -> {}", id, users);

        ProjectUpdate update = projectUpdateRepository.findById(id)
                .orElseThrow(() -> {
                    logger.warn("No project update found with ID: {}", id);
                    return new NotFoundException("No project update with the id ", id);
                });

        Set<String> tags = update.getTaggedUsers();
        tags.addAll(users);
        update.setTaggedUsers(tags);
        projectUpdateRepository.save(update);
        logger.info("Tagged users added to update ID: {}", id);
    }

    public void removeTaggedUsers(Long id, List<String> users) {
        logger.info("Removing tagged users from update ID: {} -> {}", id, users);

        ProjectUpdate update = projectUpdateRepository.findById(id)
                .orElseThrow(() -> {
                    logger.warn("No project update found with ID: {}", id);
                    return new NotFoundException("No project update with the id ", id);
                });

        Set<String> tags = update.getTaggedUsers();
        users.forEach(tags::remove);
        update.setTaggedUsers(tags);
        projectUpdateRepository.save(update);
        logger.info("Tagged users removed from update ID: {}", id);
    }
}
