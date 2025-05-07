package com.example.distribution.contoller;

import com.example.distribution.dto.ProjectUpdateRequest;
import com.example.distribution.dto.ProjectUpdateResponse;
import com.example.distribution.service.ProjectUpdateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/updates")
@CrossOrigin
public class ProjectUpdateController {

    @Autowired
    private ProjectUpdateService projectUpdateService;


    @PostMapping
    public ResponseEntity<ProjectUpdateResponse> createUpdate(@RequestBody ProjectUpdateRequest request) {
        return ResponseEntity.ok(projectUpdateService.createUpdate(request));
    }

    @GetMapping
    public ResponseEntity<List<ProjectUpdateResponse>> getUpdates() {
        return ResponseEntity.ok(projectUpdateService.getAllUpdates());
    }
}
