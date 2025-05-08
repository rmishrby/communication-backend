package com.example.distribution.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table(name = "app_user")
public class User {
    @Id
    private String username;

    private String email;
    private String phoneNumber;
}
