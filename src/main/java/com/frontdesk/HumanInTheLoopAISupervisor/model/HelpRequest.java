package com.frontdesk.HumanInTheLoopAISupervisor.model;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "help_request")
@Setter
@Getter
public class HelpRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String question;

    @Column(columnDefinition = "TEXT")
    private String supervisorAnswer;

    @Column(nullable = false)
    private String status; // PENDING, RESOLVED

    private LocalDateTime createdAt;

    public HelpRequest() {
    }

    public HelpRequest(String question) {
        this.question = question;
        this.status = "PENDING";
        this.createdAt = LocalDateTime.now();
    }
}

