package com.example.distribution.contoller;

import com.example.distribution.dto.MeetingDto;
import com.example.distribution.dto.MeetingResponseDto;
import com.example.distribution.dto.ProjectUpdateResponse;
import com.example.distribution.service.MeetingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/meeting")
@CrossOrigin
@Validated
public class MeetingController {

    @Autowired
    private MeetingService meetingService;

    @Operation(summary = "Creates meeting notes", description = "Returns a single meeting note")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation", content = @Content(schema = @Schema(implementation = ProjectUpdateResponse.class))),
    })
    @PostMapping
    public ResponseEntity<MeetingResponseDto> createMeeting(@RequestBody MeetingDto meetingDto) {
        MeetingResponseDto response = meetingService.createMeeting(meetingDto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

}
