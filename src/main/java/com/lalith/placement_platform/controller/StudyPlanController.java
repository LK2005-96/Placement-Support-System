package com.lalith.placement_platform.controller;

import com.lalith.placement_platform.dto.StudyPlanDayResponse;
import com.lalith.placement_platform.dto.StudyPlanResponse;
import com.lalith.placement_platform.entity.User;
import com.lalith.placement_platform.service.StudyPlanService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/studyplan")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class StudyPlanController {

    private final StudyPlanService studyPlanService;

    @PostMapping("/generate")
    public ResponseEntity<StudyPlanResponse> generatePlan(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(studyPlanService.generatePlan(user));
    }

    @PostMapping("/{id}/accept")
    public ResponseEntity<StudyPlanResponse> acceptPlan(
            @PathVariable Long id, @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(studyPlanService.acceptPlan(id, user));
    }

    @PostMapping("/{id}/cancel")
    public ResponseEntity<StudyPlanResponse> cancelPlan(
            @PathVariable Long id, @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(studyPlanService.cancelPlan(id, user));
    }

    @PostMapping("/day/{dayId}/toggle")
    public ResponseEntity<StudyPlanResponse> toggleDay(
            @PathVariable Long dayId, @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(studyPlanService.toggleDay(dayId, user));
    }

    @GetMapping("/current")
    public ResponseEntity<StudyPlanResponse> getCurrentPlan(@AuthenticationPrincipal User user) {
        StudyPlanResponse plan = studyPlanService.getCurrentPlan(user);
        if (plan == null) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(plan);
    }

    @GetMapping("/reminders")
    public ResponseEntity<List<StudyPlanDayResponse>> getReminders(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(studyPlanService.getReminders(user));
    }
}