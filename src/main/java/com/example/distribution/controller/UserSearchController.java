package com.example.distribution.controller;

import com.example.distribution.service.UserSearchService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.constraints.Min;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@Validated
public class UserSearchController {

    private static final Logger logger = LoggerFactory.getLogger(UserSearchController.class);

    @Autowired
    private UserSearchService userSearchService;

    @Operation(summary = "Search users by username or email",
            description = "Performs a case-insensitive full-text search for usernames or emails matching the given keyword.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved list of usernames"),
            @ApiResponse(responseCode = "400", description = "Invalid request parameters")
    })
    @GetMapping("/search")
    public ResponseEntity<List<String>> searchUsers(
            @Parameter(description = "Query string to search usernames and emails") @RequestParam String query,
            @Parameter(description = "Page number (minimum 1)") @RequestParam(defaultValue = "1") @Min(value = 1, message = "Page number should be a valid number with minimum value of 1") int page,
            @Parameter(description = "Page size (minimum 1)") @RequestParam(defaultValue = "10") @Min(value = 1, message = "Page size should be a valid number with minimum value of 1") int size) {

        logger.info("Received search request: query='{}', page={}, size={}", query, page, size);
        List<String> usernames = userSearchService.searchUsernames(query, page, size);
        return ResponseEntity.ok(usernames);

    }
}
