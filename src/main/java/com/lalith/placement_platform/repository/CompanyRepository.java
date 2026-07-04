package com.lalith.placement_platform.repository;

import com.lalith.placement_platform.entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CompanyRepository extends JpaRepository<Company, Long> {
    List<Company> findByActiveTrue();
    boolean existsByName(String name);
}