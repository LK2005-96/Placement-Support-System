package com.lalith.placement_platform.repository;

import com.lalith.placement_platform.entity.StudyPlan;
import com.lalith.placement_platform.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface StudyPlanRepository extends JpaRepository<StudyPlan, Long> {
    List<StudyPlan> findByUserOrderByCreatedAtDesc(User user);
    Optional<StudyPlan> findTopByUserOrderByCreatedAtDesc(User user);
}