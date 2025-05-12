package com.example.distribution.controller;

import com.example.distribution.dto.*;
import com.example.distribution.entity.User;
import com.example.distribution.repository.UserRepository;
import com.example.distribution.service.ProjectUpdateService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/project-updates")
@CrossOrigin
@Validated
public class ProjectUpdateController {

    private static final Logger logger = LoggerFactory.getLogger(ProjectUpdateController.class);

    @Autowired
    private ProjectUpdateService projectUpdateService;

    @Autowired
    private UserRepository userRepository;

    @Operation(summary = "Create project update", description = "Create a new update for a specific project")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Project update created successfully", content = @Content(schema = @Schema(implementation = ProjectUpdateResponse.class))),
            @ApiResponse(responseCode = "404", description = "Project not found", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping("/projects/{projectId}")
    public ResponseEntity<ProjectUpdateResponse> createProjectUpdate(
            @PathVariable Long projectId,
            @RequestBody ProjectUpdateRequest request) {
        logger.info("Creating project update for project ID: {}", projectId);
        return ResponseEntity.ok(projectUpdateService.createUpdate(projectId, request));
    }

    @Operation(summary = "List all project updates", description = "Retrieve paginated list of all project updates")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "List of project updates", content = @Content(schema = @Schema(implementation = PagedProjectUpdateResponse.class)))
    })
    @GetMapping
    public ResponseEntity<PagedProjectUpdateResponse> getAllProjectUpdates(
            @RequestParam(defaultValue = "0") @Min(0) int page,
            @RequestParam(defaultValue = "10") @Min(1) int size) {
        logger.info("Fetching all project updates: page={}, size={}", page, size);
        return ResponseEntity.ok(projectUpdateService.getAllUpdates(page, size));
    }

    @Operation(summary = "Add tagged users", description = "Add tagged users to an existing project update")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Users tagged successfully"),
            @ApiResponse(responseCode = "404", description = "Update not found", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PatchMapping("/{updateId}/tagged-users")
    public ResponseEntity<Void> addTaggedUsers(
            @PathVariable Long updateId,
            @RequestBody @NotEmpty List<String> users) {
        logger.info("Adding tagged users to update ID: {}, users: {}", updateId, users);
        projectUpdateService.addTaggedUsers(updateId, users);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Remove tagged users", description = "Remove tagged users from a project update")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Tagged users removed successfully"),
            @ApiResponse(responseCode = "404", description = "Update not found", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @DeleteMapping("/{updateId}/tagged-users")
    public ResponseEntity<Void> removeTaggedUsers(
            @PathVariable Long updateId,
            @RequestBody @NotEmpty List<String> users) {
        logger.info("Removing tagged users from update ID: {}, users: {}", updateId, users);
        projectUpdateService.removeTaggedUsers(updateId, users);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Create user", description = "Register a new user")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "User created successfully"),
            @ApiResponse(responseCode = "409", description = "User already exists", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid user data", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping("/users")
    public ResponseEntity<?> createUser(@RequestBody UserRequest request) {
        logger.info("Creating user: {}", request.getUsername());
        if (userRepository.existsById(request.getUsername())) {
            logger.warn("User already exists: {}", request.getUsername());
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new ErrorResponse(409, "User already exists"));
        }

        User user = new User(request.getUsername(), request.getEmail(), request.getRole());
        userRepository.save(user);
        logger.info("User created successfully: {}", request.getUsername());

        return ResponseEntity.status(HttpStatus.CREATED).body("User created successfully");
    }

    @Operation(summary = "Get user by username", description = "Fetch a specific user by username")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User found"),
            @ApiResponse(responseCode = "404", description = "User not found", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/users/{username}")
    public ResponseEntity<Object> getUserByUsername(@PathVariable String username) {
        logger.info("Fetching user by username: {}", username);
        return userRepository.findByUsername(username)
                .<ResponseEntity<Object>>map(user -> {
                    logger.info("User found: {}", username);
                    return ResponseEntity.ok(user);
                })
                .orElseGet(() -> {
                    logger.warn("User not found: {}", username);
                    return ResponseEntity.status(HttpStatus.NOT_FOUND)
                            .body(new ErrorResponse(404, "User not found"));
                });
    }

    @Operation(summary = "List all users", description = "Retrieve all registered users")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Users retrieved successfully")
    })
    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers() {
        logger.info("Fetching all users");
        return ResponseEntity.ok(userRepository.findAll());
    }
}
