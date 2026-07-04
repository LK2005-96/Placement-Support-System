package com.lalith.placement_platform.controller;

import com.lalith.placement_platform.entity.MockInterview;
import com.lalith.placement_platform.entity.User;
import com.lalith.placement_platform.repository.MockInterviewRepository;
import com.lalith.placement_platform.service.MockInterviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/interview")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class MockInterviewController {

    private final MockInterviewService mockInterviewService;
    private final MockInterviewRepository mockInterviewRepository;

    @PostMapping("/start")
    public ResponseEntity<MockInterview> startInterview(
            @RequestParam String targetRole,
            @RequestParam String targetCompany,
            @AuthenticationPrincipal User user) {

        MockInterview interview = mockInterviewService.generateQuestions(targetRole, targetCompany, user);
        return ResponseEntity.ok(interview);
    }

    @PostMapping("/submit/{interviewId}")
public ResponseEntity<MockInterview> submitAnswers(
        @PathVariable Long interviewId,
        @RequestBody String answers,
        @AuthenticationPrincipal User user) {

    MockInterview interview = mockInterviewService.submitAnswers(interviewId, answers, user);
    return ResponseEntity.ok(interview);
}
    @GetMapping("/history")
    public ResponseEntity<List<MockInterview>> getHistory(
            @AuthenticationPrincipal User user) {

        List<MockInterview> interviews = mockInterviewRepository.findByUserOrderByCreatedAtDesc(user);
        return ResponseEntity.ok(interviews);
    }
}