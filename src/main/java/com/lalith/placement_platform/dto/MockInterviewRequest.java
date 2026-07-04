package com.lalith.placement_platform.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class MockInterviewRequest {

    @NotBlank(message = "Target role is required")
    private String targetRole;

    @NotBlank(message = "Target company is required")
    private String targetCompany;

    private String answers;
}