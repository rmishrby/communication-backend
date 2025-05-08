package com.example.distribution.exception;


public class ProjectUpdateNotFoundException extends RuntimeException {
    public ProjectUpdateNotFoundException(Long id) {
        super("No project update with the id " + id);
    }
}
