package com.lalith.placement_platform.service;

import com.lalith.placement_platform.dto.StudyPlanDayResponse;
import com.lalith.placement_platform.dto.StudyPlanResponse;
import com.lalith.placement_platform.entity.Resume;
import com.lalith.placement_platform.entity.StudyPlan;
import com.lalith.placement_platform.entity.StudyPlanDay;
import com.lalith.placement_platform.entity.User;
import com.lalith.placement_platform.repository.ResumeRepository;
import com.lalith.placement_platform.repository.StudyPlanDayRepository;
import com.lalith.placement_platform.repository.StudyPlanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StudyPlanService {

    private final GeminiApiService geminiApiService;
    private final ResumeRepository resumeRepository;
    private final StudyPlanRepository studyPlanRepository;
    private final StudyPlanDayRepository studyPlanDayRepository;

    public StudyPlanResponse generatePlan(User user) {
        Resume resume = resumeRepository.findTopByUserOrderByUploadedAtDesc(user)
                .orElseThrow(() -> new RuntimeException("Please upload and analyse your resume first"));

        String skillGap = resume.getSkillGapAnalysis();

        String prompt = """
                A student has the following skill gaps for a software engineering role:
                %s

                Create a 30-day study plan to close these gaps. One clear, specific, actionable task per day.
                Format EXACTLY like this, one line per day, no extra commentary:
                Day 1: <task>
                Day 2: <task>
                ...continue through...
                Day 30: <task>
                """.formatted(skillGap);

        String aiResponse = geminiApiService.askGemini(prompt);

        List<String> tasks = parseDayTasks(aiResponse);

        StudyPlan plan = new StudyPlan();
        plan.setUser(user);
        plan.setStatus(StudyPlan.Status.PROPOSED);
        studyPlanRepository.save(plan);

        List<StudyPlanDay> days = new ArrayList<>();
        for (int i = 0; i < tasks.size(); i++) {
            StudyPlanDay day = new StudyPlanDay();
            day.setStudyPlan(plan);
            day.setDayNumber(i + 1);
            day.setTask(tasks.get(i));
            days.add(day);
        }
        studyPlanDayRepository.saveAll(days);

        return toResponse(plan, days);
    }

    private List<String> parseDayTasks(String text) {
        List<String> tasks = new ArrayList<>();
        Pattern pattern = Pattern.compile("Day\\s*(\\d+):\\s*(.+)");
        for (String line : text.split("\n")) {
            Matcher matcher = pattern.matcher(line.trim());
            if (matcher.find()) {
                tasks.add(matcher.group(2).trim());
            }
        }
        return tasks;
    }

    public StudyPlanResponse acceptPlan(Long planId, User user) {
        StudyPlan plan = getOwnedPlan(planId, user);
        plan.setStatus(StudyPlan.Status.ACCEPTED);
        plan.setStartDate(LocalDate.now());
        studyPlanRepository.save(plan);
        List<StudyPlanDay> days = studyPlanDayRepository.findByStudyPlanOrderByDayNumberAsc(plan);
        return toResponse(plan, days);
    }

    public StudyPlanResponse cancelPlan(Long planId, User user) {
        StudyPlan plan = getOwnedPlan(planId, user);
        plan.setStatus(StudyPlan.Status.CANCELLED);
        studyPlanRepository.save(plan);
        List<StudyPlanDay> days = studyPlanDayRepository.findByStudyPlanOrderByDayNumberAsc(plan);
        return toResponse(plan, days);
    }

    public StudyPlanResponse toggleDay(Long dayId, User user) {
        StudyPlanDay day = studyPlanDayRepository.findById(dayId)
                .orElseThrow(() -> new RuntimeException("Day not found"));

        if (!day.getStudyPlan().getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Not your study plan");
        }

        day.setCompleted(!day.isCompleted());
        day.setCompletedAt(day.isCompleted() ? java.time.LocalDateTime.now() : null);
        studyPlanDayRepository.save(day);

        StudyPlan plan = day.getStudyPlan();
        List<StudyPlanDay> days = studyPlanDayRepository.findByStudyPlanOrderByDayNumberAsc(plan);
        return toResponse(plan, days);
    }

    public StudyPlanResponse getCurrentPlan(User user) {
        List<StudyPlan> plans = studyPlanRepository.findByUserOrderByCreatedAtDesc(user);
        StudyPlan latest = plans.stream()
                .filter(p -> p.getStatus() != StudyPlan.Status.CANCELLED)
                .findFirst()
                .orElse(null);

        if (latest == null) return null;

        List<StudyPlanDay> days = studyPlanDayRepository.findByStudyPlanOrderByDayNumberAsc(latest);
        return toResponse(latest, days);
    }

    public List<StudyPlanDayResponse> getReminders(User user) {
        List<StudyPlan> plans = studyPlanRepository.findByUserOrderByCreatedAtDesc(user);
        StudyPlan accepted = plans.stream()
                .filter(p -> p.getStatus() == StudyPlan.Status.ACCEPTED)
                .findFirst()
                .orElse(null);

        if (accepted == null) return List.of();

        List<StudyPlanDay> days = studyPlanDayRepository.findByStudyPlanOrderByDayNumberAsc(accepted);
        LocalDate today = LocalDate.now();

        return days.stream()
                .map(d -> toDayResponse(d, accepted.getStartDate()))
                .filter(d -> d.isOverdue())
                .collect(Collectors.toList());
    }

    private StudyPlan getOwnedPlan(Long planId, User user) {
        StudyPlan plan = studyPlanRepository.findById(planId)
                .orElseThrow(() -> new RuntimeException("Study plan not found"));
        if (!plan.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Not your study plan");
        }
        return plan;
    }

    private StudyPlanDayResponse toDayResponse(StudyPlanDay day, LocalDate startDate) {
        StudyPlanDayResponse dto = new StudyPlanDayResponse();
        dto.setId(day.getId());
        dto.setDayNumber(day.getDayNumber());
        dto.setTask(day.getTask());
        dto.setCompleted(day.isCompleted());
        dto.setCompletedAt(day.getCompletedAt());

        if (startDate != null) {
            LocalDate dueDate = startDate.plusDays(day.getDayNumber() - 1);
            dto.setDueDate(dueDate);
            dto.setOverdue(!day.isCompleted() && dueDate.isBefore(LocalDate.now()));
        }

        return dto;
    }

    private StudyPlanResponse toResponse(StudyPlan plan, List<StudyPlanDay> days) {
        StudyPlanResponse dto = new StudyPlanResponse();
        dto.setId(plan.getId());
        dto.setStatus(plan.getStatus().name());
        dto.setStartDate(plan.getStartDate());
        dto.setCreatedAt(plan.getCreatedAt());
        dto.setDays(days.stream()
                .map(d -> toDayResponse(d, plan.getStartDate()))
                .collect(Collectors.toList()));
        return dto;
    }
}