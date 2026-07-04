package com.lalith.placement_platform.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class RoundResponse {
    private Long id;
    private Long studentId;
    private String studentUsername;
    private String studentFullName;
    private Long companyId;
    private String companyName;
    private String roundType;
    private String status;
    private String remarks;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}