package com.lalith.placement_platform.service;

import com.lalith.placement_platform.entity.MockInterview;
import com.lalith.placement_platform.entity.User;
import com.lalith.placement_platform.repository.MockInterviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MockInterviewService {

    private final GeminiApiService geminiApiService;
    private final MockInterviewRepository mockInterviewRepository;

    public MockInterview generateQuestions(String targetRole, String targetCompany, User user) {
        String prompt = """
                You are an expert technical interviewer at %s.
                Generate exactly 5 technical interview questions for a %s position.
                Format your response as:
                Q1: <question>
                Q2: <question>
                Q3: <question>
                Q4: <question>
                Q5: <question>
                """.formatted(targetCompany, targetRole);

        String questions = geminiApiService.askGemini(prompt);

        MockInterview interview = new MockInterview();
        interview.setUser(user);
        interview.setTargetRole(targetRole);
        interview.setTargetCompany(targetCompany);
        interview.setQuestions(questions);

        return mockInterviewRepository.save(interview);
    }

    public MockInterview submitAnswers(Long interviewId, String answers, User user) {
        MockInterview interview = mockInterviewRepository.findById(interviewId)
                .orElseThrow(() -> new RuntimeException("Interview not found"));

        String prompt = """
                You are an expert technical interviewer. Evaluate these answers:

                QUESTIONS:
                %s

                CANDIDATE ANSWERS:
                %s

                Provide feedback in this format:
                OVERALL: <overall assessment in 2-3 sentences>
                Q1_FEEDBACK: <feedback for answer 1>
                Q2_FEEDBACK: <feedback for answer 2>
                Q3_FEEDBACK: <feedback for answer 3>
                Q4_FEEDBACK: <feedback for answer 4>
                Q5_FEEDBACK: <feedback for answer 5>
                SCORE: <score out of 10>
                """.formatted(interview.getQuestions(), answers);

        String feedback = geminiApiService.askGemini(prompt);

        interview.setAnswers(answers);
        interview.setFeedback(feedback);

        return mockInterviewRepository.save(interview);
    }
}