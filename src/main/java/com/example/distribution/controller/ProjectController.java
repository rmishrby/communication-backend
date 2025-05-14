package com.example.distribution.controller;

import com.example.distribution.dto.*;
import com.example.distribution.service.ProjectService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/projects")
@CrossOrigin
public class ProjectController {

    private static final Logger logger = LoggerFactory.getLogger(ProjectController.class);

    @Autowired
    private ProjectService projectService;

    @Operation(summary = "Create a new project", description = "Creates a project and returns its metadata")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Project created successfully", content = @Content(schema = @Schema(implementation = ProjectResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid project input", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping
    public ResponseEntity<ProjectResponse> createProject(@RequestBody ProjectRequest request) {
        logger.info("Creating project: {}", request.getTitle());
        ProjectResponse response = projectService.createProject(request);
        logger.info("Project created with ID: {}", response.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "List all projects", description = "Returns a list of all projects without updates")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Projects retrieved successfully", content = @Content(array = @ArraySchema(schema = @Schema(implementation = ProjectResponse.class))))
    })
    @GetMapping
    public ResponseEntity<List<ProjectSummaryResponse>> getAllProjects() {
        logger.info("Fetching all projects");
        List<ProjectSummaryResponse> projects = projectService.getAllProjectsSummary();
        logger.info("Found {} projects", projects.size());
        return ResponseEntity.ok(projects);
    }

    @Operation(summary = "Get project by ID", description = "Returns full project details including updates")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Project details retrieved successfully", content = @Content(schema = @Schema(implementation = ProjectDetailResponse.class))),
            @ApiResponse(responseCode = "404", description = "Project not found", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/{id}")
    public ResponseEntity<ProjectDetailResponse> getProjectById(@PathVariable Long id) {
        logger.info("Fetching project by ID: {}", id);
        ProjectDetailResponse response = projectService.getProjectById(id);
        logger.info("Project details retrieved for ID: {}", id);
        return ResponseEntity.ok(response);
    }
}
