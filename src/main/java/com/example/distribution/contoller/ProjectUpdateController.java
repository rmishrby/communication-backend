package com.example.distribution.contoller;

import com.example.distribution.dto.ErrorResponse;
import com.example.distribution.dto.PagedProjectUpdateResponse;
import com.example.distribution.dto.ProjectUpdateRequest;
import com.example.distribution.dto.ProjectUpdateResponse;
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
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/updates")
@CrossOrigin
@Validated
public class ProjectUpdateController {

    @Autowired
    private ProjectUpdateService projectUpdateService;

    @Operation(summary = "Post a meeting note", description = "Returns a single meeting note")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation", content = @Content(schema = @Schema(implementation = ProjectUpdateResponse.class))),
    })
    @PostMapping
    public ResponseEntity<ProjectUpdateResponse> createUpdate(@RequestBody ProjectUpdateRequest request) {
        return ResponseEntity.ok(projectUpdateService.createUpdate(request));
    }
    @Operation(
            summary = "Get all project updates",
            description = "Returns a list of project updates"
    )
    @ApiResponses(value = {
            @ApiResponse( responseCode = "200", description = "Successful operation", content = @Content(array = @ArraySchema(schema = @Schema(implementation = PagedProjectUpdateResponse.class))))
    })
    @GetMapping
    public ResponseEntity<PagedProjectUpdateResponse> getUpdates(
            @RequestParam(defaultValue = "0") @Min(value = 0, message = "page number should be a valid number with minimum value of 0") int page,
            @RequestParam(defaultValue = "10") @Min(value = 1, message = "Page size should be a valid number with minimum value of 1") int size
    ) {
        return ResponseEntity.ok(projectUpdateService.getAllUpdates(page, size));
    }

    @Operation(
            summary = "Add tagged users to a project update",
            description = "Adds one or more users to the tagged user list of a specific project update by ID"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tagged users added successfully"),
            @ApiResponse(responseCode = "404", description = "Project update not found", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PatchMapping("/{id}/tags/add")
    public ResponseEntity<?> addTaggedUsers(@PathVariable Long id, @RequestBody @NotEmpty List<String> users ) {
        projectUpdateService.addTaggedUsers(id, users);
        return ResponseEntity.ok().build();
    }

    @Operation(
            summary = "Remove tagged users from a project update",
            description = "Removes one or more users from the tagged user list of a specific project update by ID"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tagged users removed successfully"),
            @ApiResponse(responseCode = "404", description = "Project update not found", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })

    @PatchMapping("/{id}/tags/remove")
    public ResponseEntity<?> removeTaggedUsers(@PathVariable Long id, @RequestBody @NotEmpty List<String> users) {
        projectUpdateService.removeTaggedUsers(id, users);
        return ResponseEntity.ok().build();
    }
}
