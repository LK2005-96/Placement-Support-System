package com.lalith.placement_platform.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "study_plan_days")
@Data
public class StudyPlanDay {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "study_plan_id")
    private StudyPlan studyPlan;

    private int dayNumber;

    @Column(columnDefinition = "TEXT")
    private String task;

    private boolean completed = false;

    private LocalDateTime completedAt;
}