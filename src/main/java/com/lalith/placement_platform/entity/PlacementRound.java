package com.lalith.placement_platform.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "placement_rounds")
@Data
public class PlacementRound {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "company_id")
    private Company company;

    @ManyToOne
    @JoinColumn(name = "student_id")
    @com.fasterxml.jackson.annotation.JsonIgnore
    private User student;

    @Enumerated(EnumType.STRING)
    private RoundType roundType;

    @Enumerated(EnumType.STRING)
    private RoundStatus status;

    private String remarks;

    @Column(updatable = false)
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @PrePersist
    public void prePersist() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public enum RoundType {
        APTITUDE, CODING, TECHNICAL_INTERVIEW, HR_INTERVIEW, FINAL_RESULT
    }

    public enum RoundStatus {
        PENDING, PASS, FAIL, SELECTED
    }
}