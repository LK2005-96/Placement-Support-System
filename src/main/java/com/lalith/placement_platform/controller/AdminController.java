package com.lalith.placement_platform.controller;

import com.lalith.placement_platform.dto.StudentDto;
import com.lalith.placement_platform.entity.PlacementRound;
import com.lalith.placement_platform.entity.User;
import com.lalith.placement_platform.dto.StudentDto;
import com.lalith.placement_platform.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AdminController {

    private final AdminService adminService;

    @GetMapping("/dashboard")
    public ResponseEntity<Map<String, Object>> getDashboardStats() {
        return ResponseEntity.ok(adminService.getDashboardStats());
    }

   @GetMapping("/students")
public ResponseEntity<List<StudentDto>> getAllStudents() {
    return ResponseEntity.ok(adminService.getAllStudents());
}

    @GetMapping("/rounds/company/{companyId}")
    public ResponseEntity<List<com.lalith.placement_platform.dto.RoundResponse>> getRoundsByCompany(
            @PathVariable Long companyId) {
        return ResponseEntity.ok(adminService.getRoundsByCompany(companyId));
    }

    @GetMapping("/rounds/student/{studentId}")
    public ResponseEntity<List<com.lalith.placement_platform.dto.RoundResponse>> getRoundsByStudent(
            @PathVariable Long studentId) {
        return ResponseEntity.ok(adminService.getRoundsByStudent(studentId));
    }
    @PostMapping("/rounds/add")
    public ResponseEntity<PlacementRound> addStudentToRound(
            @RequestParam Long studentId,
            @RequestParam Long companyId,
            @RequestParam PlacementRound.RoundType roundType) {
        return ResponseEntity.ok(
                adminService.addStudentToRound(studentId, companyId, roundType));
    }

    @PutMapping("/rounds/{roundId}")
    public ResponseEntity<PlacementRound> updateRoundStatus(
            @PathVariable Long roundId,
            @RequestParam PlacementRound.RoundStatus status,
            @RequestParam(required = false) String remarks) {
        return ResponseEntity.ok(
                adminService.updateRoundStatus(roundId, status, remarks));
    }
}