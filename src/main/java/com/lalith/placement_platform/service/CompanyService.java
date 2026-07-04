package com.lalith.placement_platform.service;

import com.lalith.placement_platform.entity.Company;
import com.lalith.placement_platform.repository.CompanyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CompanyService {

    private final CompanyRepository companyRepository;

    public Company addCompany(Company company) {
        if (companyRepository.existsByName(company.getName())) {
            throw new RuntimeException("Company already exists");
        }
        return companyRepository.save(company);
    }

    public List<Company> getAllCompanies() {
        return companyRepository.findAll();
    }

    public List<Company> getActiveCompanies() {
        return companyRepository.findByActiveTrue();
    }

    public Company updateCompany(Long id, Company updated) {
        Company company = companyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Company not found"));
        company.setName(updated.getName());
        company.setDescription(updated.getDescription());
        company.setWebsite(updated.getWebsite());
        company.setEligibility(updated.getEligibility());
        company.setMinCgpa(updated.getMinCgpa());
        company.setHiringRoles(updated.getHiringRoles());
        company.setHiringSchedule(updated.getHiringSchedule());
        company.setActive(updated.isActive());
        return companyRepository.save(company);
    }

    public void deleteCompany(Long id) {
        companyRepository.deleteById(id);
    }

    public Company getCompanyById(Long id) {
        return companyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Company not found"));
    }
}