package com.example.distribution.entity;

import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Entity
public class ActionItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String text;
    private boolean done;

    @ManyToOne
    @JoinColumn(name = "meeting_id")
    private Meeting meeting;
}