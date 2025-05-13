package com.example.distribution.controller;

import com.example.distribution.dto.ErrorResponse;
import com.example.distribution.dto.UserRequest;
import com.example.distribution.entity.User;
import com.example.distribution.repository.UserRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@CrossOrigin
@Validated
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserRepository userRepository;

    @Operation(summary = "Create user", description = "Register a new user")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "User created successfully"),
            @ApiResponse(responseCode = "409", description = "User already exists", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid user data", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping
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
    @GetMapping("/{username}")
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
    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        logger.info("Fetching all users");
        return ResponseEntity.ok(userRepository.findAll());
    }
}
