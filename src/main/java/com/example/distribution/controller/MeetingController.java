package com.example.distribution.controller;

import com.example.distribution.dto.MeetingDto;
import com.example.distribution.dto.MeetingResponseDto;
import com.example.distribution.dto.ProjectUpdateResponse;
import com.example.distribution.service.MeetingService;
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
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/meeting")
@CrossOrigin
@Validated
public class MeetingController {

    private static final Logger logger = LoggerFactory.getLogger(MeetingController.class);

    @Autowired
    private MeetingService meetingService;

    @Operation(summary = "Creates meeting notes", description = "Returns a single meeting note")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation", content = @Content(schema = @Schema(implementation = ProjectUpdateResponse.class))),
    })
    @PostMapping
    public ResponseEntity<MeetingResponseDto> createMeeting(@RequestBody MeetingDto meetingDto) {
        logger.info("Creating meeting: {}", meetingDto);
        MeetingResponseDto response = meetingService.createMeeting(meetingDto);
        logger.info("Meeting created with ID: {}", response.getId());
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @Operation(summary = "Updates meeting notes", description = "Updates an existing meeting note by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully updated", content = @Content(schema = @Schema(implementation = MeetingResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "Meeting not found", content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<MeetingResponseDto> updateMeeting(@PathVariable Long id, @RequestBody MeetingDto dto) {
        logger.info("Updating meeting ID: {}", id);
        MeetingResponseDto response = meetingService.updateMeeting(id, dto);
        logger.info("Meeting updated: {}", response.getId());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Operation(summary = "Deletes meeting notes", description = "Deletes a meeting note by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Successfully deleted"),
            @ApiResponse(responseCode = "404", description = "Meeting not found", content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMeeting(@PathVariable Long id) {
        logger.info("Deleting meeting ID: {}", id);
        meetingService.deleteMeeting(id);
        logger.info("Meeting deleted successfully: {}", id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Operation(summary = "Retrieves all meeting notes", description = "Returns a list of all meeting notes")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved", content = @Content(array = @ArraySchema(schema = @Schema(implementation = MeetingResponseDto.class))))
    })
    @GetMapping("/{username}")
    public ResponseEntity<List<MeetingResponseDto>> getAllMeetings(@PathVariable String username) {
        logger.info("Fetching all meeting notes");
        List<MeetingResponseDto> meetings = meetingService.getMeetingsByUsername(username);
        logger.info("Total meetings retrieved: {}", meetings.size());
        return new ResponseEntity<>(meetings, HttpStatus.OK);
    }
}
