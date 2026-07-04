package com.lalith.placement_platform.repository;

import com.lalith.placement_platform.entity.StudyPlan;
import com.lalith.placement_platform.entity.StudyPlanDay;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StudyPlanDayRepository extends JpaRepository<StudyPlanDay, Long> {
    List<StudyPlanDay> findByStudyPlanOrderByDayNumberAsc(StudyPlan studyPlan);
}