package com.lalith.placement_platform.controller;

import com.lalith.placement_platform.dto.ResumeResponse;
import com.lalith.placement_platform.entity.Resume;
import com.lalith.placement_platform.entity.User;
import com.lalith.placement_platform.repository.ResumeRepository;
import com.lalith.placement_platform.service.ResumeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/resume")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ResumeController {

    private final ResumeService resumeService;
    private final ResumeRepository resumeRepository;

    @PostMapping("/upload")
    public ResponseEntity<ResumeResponse> uploadResume(
            @RequestParam("file") MultipartFile file,
            @AuthenticationPrincipal User user) {

        Resume resume = resumeService.analyzeResume(file, user);
        return ResponseEntity.ok(toResponse(resume));
    }

    @GetMapping("/history")
    public ResponseEntity<List<ResumeResponse>> getHistory(
            @AuthenticationPrincipal User user) {

        List<Resume> resumes = resumeRepository.findByUser(user);
        return ResponseEntity.ok(resumes.stream().map(this::toResponse).toList());
    }

    private ResumeResponse toResponse(Resume resume) {
        ResumeResponse response = new ResumeResponse();
        response.setId(resume.getId());
        response.setFileName(resume.getFileName());
        response.setAiReview(resume.getAiReview());
        response.setSkillGapAnalysis(resume.getSkillGapAnalysis());
        response.setRoadmap(resume.getRoadmap());
        response.setAtsScore(resume.getAtsScore());
        response.setUploadedAt(resume.getUploadedAt());
        return response;
    }
}