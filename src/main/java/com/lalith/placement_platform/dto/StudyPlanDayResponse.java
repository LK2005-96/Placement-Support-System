package com.lalith.placement_platform.dto;

import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class StudyPlanDayResponse {
    private Long id;
    private int dayNumber;
    private String task;
    private boolean completed;
    private LocalDateTime completedAt;
    private LocalDate dueDate;
    private boolean overdue;
}