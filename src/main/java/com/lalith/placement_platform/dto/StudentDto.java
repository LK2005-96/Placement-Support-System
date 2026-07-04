package com.lalith.placement_platform.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class StudentDto {
    private Long id;
    private String username;
    private String email;
    private String fullName;
    private String role;
    private LocalDateTime createdAt;
}