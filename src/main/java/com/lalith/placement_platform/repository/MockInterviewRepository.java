package com.lalith.placement_platform.repository;

import com.lalith.placement_platform.entity.MockInterview;
import com.lalith.placement_platform.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MockInterviewRepository extends JpaRepository<MockInterview, Long> {
    List<MockInterview> findByUserOrderByCreatedAtDesc(User user);
}