package com.lalith.placement_platform.repository;

import com.lalith.placement_platform.entity.PlacementRound;
import com.lalith.placement_platform.entity.User;
import com.lalith.placement_platform.entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PlacementRoundRepository extends JpaRepository<PlacementRound, Long> {
    List<PlacementRound> findByStudent(User student);
    List<PlacementRound> findByCompany(Company company);
    List<PlacementRound> findByCompanyAndRoundType(Company company, PlacementRound.RoundType roundType);
    long countByStatusAndCompany(PlacementRound.RoundStatus status, Company company);
}