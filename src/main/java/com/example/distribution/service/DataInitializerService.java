package com.example.distribution.service;

import com.example.distribution.entity.User;
import com.example.distribution.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializerService implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;
    @Override
    public void run(String... args) {
        if (!userRepository.existsById("rmishra")) {
            User user = new User("rmishra", "1806231@kiit.ac.in", "admin");
            userRepository.save(user);
            System.out.println("Default admin user created.");
        } else {
            System.out.println("Admin user already exists.");
        }
    }
}
