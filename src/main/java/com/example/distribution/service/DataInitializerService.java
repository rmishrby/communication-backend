package com.example.distribution.service;

import com.example.distribution.entity.User;
import com.example.distribution.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializerService implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(DataInitializerService.class);

    @Autowired
    private UserRepository userRepository;

    @Override
    public void run(String... args) {
        if (!userRepository.existsById("rmishra")) {
            User user = new User("rmishra", "1806231@kiit.ac.in", "admin");
            userRepository.save(user);
            logger.info("Default admin user created.");
        } else {
            logger.info("Admin user already exists.");
        }
    }
}
