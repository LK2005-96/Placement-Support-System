package com.lalith.placement_platform.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CodingService {

    private final GeminiApiService geminiApiService;

    public Map<String, String> getRecommendations(String targetCompany, String difficulty) {
        String topicsPrompt = "List the top 5 coding topics a candidate should focus on for " +
                targetCompany + " placement interviews at " + difficulty + " difficulty. " +
                "Format as a numbered list with brief explanation for each topic.";

        String problemsPrompt = "Give 5 specific coding problems (LeetCode style) that are " +
                "commonly asked at " + targetCompany + " at " + difficulty + " difficulty. " +
                "For each problem include: problem name, brief description, and key approach to solve it.";

        String tipsPrompt = "Give 5 specific tips for cracking the coding round at " +
                targetCompany + ". Focus on what makes " + targetCompany +
                " unique in their hiring process and what they look for in candidates.";

        String topics = geminiApiService.askGemini(topicsPrompt);
        String problems = geminiApiService.askGemini(problemsPrompt);
        String tips = geminiApiService.askGemini(tipsPrompt);

        Map<String, String> result = new HashMap<>();
        result.put("topics", topics);
        result.put("problems", problems);
        result.put("tips", tips);

        return result;
    }
}