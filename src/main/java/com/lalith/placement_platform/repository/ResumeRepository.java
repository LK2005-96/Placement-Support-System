package com.lalith.placement_platform.repository;

import com.lalith.placement_platform.entity.Resume;
import com.lalith.placement_platform.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ResumeRepository extends JpaRepository<Resume, Long> {
    List<Resume> findByUser(User user);
    Optional<Resume> findTopByUserOrderByUploadedAtDesc(User user);
}