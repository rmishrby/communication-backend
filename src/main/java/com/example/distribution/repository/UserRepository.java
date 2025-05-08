package com.example.distribution.repository;

import com.example.distribution.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    List<User> findByUsernameIn(Set<String> usernames);
}