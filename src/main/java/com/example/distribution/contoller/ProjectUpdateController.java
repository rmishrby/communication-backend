package com.example.distribution.contoller;

import com.example.distribution.dto.*;
import com.example.distribution.entity.User;
import com.example.distribution.repository.UserRepository;
import com.example.distribution.service.ProjectUpdateService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/updates")
@CrossOrigin
@Validated
public class ProjectUpdateController {

    @Autowired
    private ProjectUpdateService projectUpdateService;

    @Autowired
    private UserRepository userRepository;

    @Operation(summary = "Create a project update", description = "Creates and returns a new project update")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation", content = @Content(schema = @Schema(implementation = ProjectUpdateResponse.class))),
    })
    @PostMapping
    public ResponseEntity<ProjectUpdateResponse> create(@RequestBody ProjectUpdateRequest request) {
        return ResponseEntity.ok(projectUpdateService.createUpdate(request));
    }

    @Operation(summary = "List project updates", description = "Returns a paginated list of project updates")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation", content = @Content(array = @ArraySchema(schema = @Schema(implementation = PagedProjectUpdateResponse.class))))
    })
    @GetMapping
    public ResponseEntity<PagedProjectUpdateResponse> list(
            @RequestParam(defaultValue = "0") @Min(value = 0, message = "Page number must be at least 0") int page,
            @RequestParam(defaultValue = "10") @Min(value = 1, message = "Page size must be at least 1") int size
    ) {
        return ResponseEntity.ok(projectUpdateService.getAllUpdates(page, size));
    }

    @Operation(summary = "Add tagged users", description = "Adds users to the tagged user list of a project update")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tagged users added successfully"),
            @ApiResponse(responseCode = "404", description = "Project update not found", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PatchMapping("/{id}/tags")
    public ResponseEntity<?> addTags(@PathVariable Long id, @RequestBody @NotEmpty List<String> users) {
        projectUpdateService.addTaggedUsers(id, users);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Remove tagged users", description = "Removes users from the tagged user list of a project update")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tagged users removed successfully"),
            @ApiResponse(responseCode = "404", description = "Project update not found", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @DeleteMapping("/{id}/tags")
    public ResponseEntity<?> removeTags(@PathVariable Long id, @RequestBody @NotEmpty List<String> users) {
        projectUpdateService.removeTaggedUsers(id, users);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Create a user", description = "Adds a new user if the username doesn't already exist")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User created successfully"),
            @ApiResponse(responseCode = "409", description = "User already exists", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping("/users")
    public ResponseEntity<?> createUser(@RequestBody UserRequest userRequest) {
        if (userRepository.existsById(userRequest.getUsername())) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new ErrorResponse(409, "User already exists"));
        }

        User user = new User(
                userRequest.getUsername(),
                userRequest.getEmail(),
                userRequest.getRole()
        );
        userRepository.save(user);

        return ResponseEntity.status(HttpStatus.CREATED).body("User created successfully");
    }

    @Operation(summary = "Get a user by username", description = "Fetches a user by username")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User fetched successfully"),
            @ApiResponse(responseCode = "404", description = "User not found", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/users/{username}")
    public ResponseEntity<?> getUser(@PathVariable String username) {
        return userRepository.findByUsername(username)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "List all users", description = "Returns a list of all users")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "All users fetched successfully"),
    })
    @GetMapping("/users")
    public ResponseEntity<List<User>> listUsers() {
        List<User> users = userRepository.findAll();
        return ResponseEntity.ok(users);
    }

}
