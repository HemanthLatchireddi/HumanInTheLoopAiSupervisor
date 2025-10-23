package com.frontdesk.HumanInTheLoopAISupervisor.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "knowledge", uniqueConstraints = @UniqueConstraint(columnNames = "question"))
@Getter
@Setter
public class Knowledge {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String question;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String answer;

    public Knowledge() {
    }

    public Knowledge(String question, String answer) {
        this.question = question;
        this.answer = answer;
    }
}
