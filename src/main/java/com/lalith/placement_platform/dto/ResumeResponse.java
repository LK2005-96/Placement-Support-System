package com.lalith.placement_platform.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ResumeResponse {
    private Long id;
    private String fileName;
    private String aiReview;
    private String skillGapAnalysis;
    private String roadmap;
    private Integer atsScore;
    private LocalDateTime uploadedAt;
}