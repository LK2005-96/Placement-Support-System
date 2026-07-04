package com.lalith.placement_platform.dto;

import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class StudyPlanResponse {
    private Long id;
    private String status;
    private LocalDate startDate;
    private LocalDateTime createdAt;
    private List<StudyPlanDayResponse> days;
}