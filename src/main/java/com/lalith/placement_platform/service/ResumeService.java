package com.lalith.placement_platform.service;

import com.lalith.placement_platform.entity.Resume;
import com.lalith.placement_platform.entity.User;
import com.lalith.placement_platform.repository.ResumeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class ResumeService {

    private final PdfService pdfService;
    private final GeminiApiService geminiApiService;
    private final ResumeRepository resumeRepository;

    public Resume analyzeResume(MultipartFile file, User user) {
        String extractedText = pdfService.extractText(file);

        String prompt = """
                You are an expert technical recruiter and resume reviewer.
                Analyze the following resume text and respond in EXACTLY this format:

                ATS_SCORE: <a number from 0-100>
                REVIEW: <detailed review of strengths and weaknesses, 3-5 sentences>
                SKILL_GAP: <list of missing skills for a software engineering / data role, comma separated>
                ROADMAP: <a short 4-6 step improvement roadmap>

                Resume text:
                %s
                """.formatted(extractedText);

        String aiResponse = geminiApiService.askGemini(prompt);

        Resume resume = new Resume();
        resume.setUser(user);
        resume.setFileName(file.getOriginalFilename());
        resume.setExtractedText(extractedText);
        resume.setAtsScore(extractField(aiResponse, "ATS_SCORE"));
        resume.setAiReview(extractSection(aiResponse, "REVIEW", "SKILL_GAP"));
        resume.setSkillGapAnalysis(extractSection(aiResponse, "SKILL_GAP", "ROADMAP"));
        resume.setRoadmap(extractSection(aiResponse, "ROADMAP", null));

        return resumeRepository.save(resume);
    }

    private Integer extractField(String text, String field) {
        Pattern pattern = Pattern.compile(field + ":\\s*(\\d+)");
        Matcher matcher = pattern.matcher(text);
        if (matcher.find()) {
            return Integer.parseInt(matcher.group(1));
        }
        return 0;
    }

    private String extractSection(String text, String startField, String endField) {
        int startIdx = text.indexOf(startField + ":");
        if (startIdx == -1) return "";
        startIdx += (startField + ":").length();

        int endIdx = text.length();
        if (endField != null) {
            int found = text.indexOf(endField + ":", startIdx);
            if (found != -1) endIdx = found;
        }

        return text.substring(startIdx, endIdx).trim();
    }
}