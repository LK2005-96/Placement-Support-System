package com.lalith.placement_platform.service;

import com.lalith.placement_platform.dto.RoundResponse;
import com.lalith.placement_platform.dto.StudentDto;
import com.lalith.placement_platform.entity.Company;
import com.lalith.placement_platform.entity.PlacementRound;
import com.lalith.placement_platform.entity.User;
import com.lalith.placement_platform.repository.CompanyRepository;
import com.lalith.placement_platform.repository.PlacementRoundRepository;
import com.lalith.placement_platform.repository.UserRepository;
import lombok.RequiredArgsConstructor;
//import com.lalith.placement_platform.dto.RoundResponse;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final UserRepository userRepository;
    private final CompanyRepository companyRepository;
    private final PlacementRoundRepository placementRoundRepository;

    // Get dashboard statistics
    public Map<String, Object> getDashboardStats() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalStudents", userRepository.count());
        stats.put("totalCompanies", companyRepository.count());
        stats.put("activeCompanies", companyRepository.findByActiveTrue().size());
        stats.put("totalRounds", placementRoundRepository.count());
        return stats;
    }

    // Get all students — no passwords exposed
    public List<StudentDto> getAllStudents() {
        return userRepository.findAll()
                .stream()
                .map(this::toStudentDto)
                .collect(Collectors.toList());
    }

    private StudentDto toStudentDto(User user) {
        StudentDto dto = new StudentDto();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        dto.setFullName(user.getFullName());
        dto.setRole(user.getRole().name());
        dto.setCreatedAt(user.getCreatedAt());
        return dto;
    }

    // Update placement round status
    public PlacementRound updateRoundStatus(Long roundId,
            PlacementRound.RoundStatus status, String remarks) {
        PlacementRound round = placementRoundRepository.findById(roundId)
                .orElseThrow(() -> new RuntimeException("Round not found"));
        round.setStatus(status);
        round.setRemarks(remarks);
        return placementRoundRepository.save(round);
    }

    // Add student to placement round
    public PlacementRound addStudentToRound(Long studentId, Long companyId,
            PlacementRound.RoundType roundType) {
        User student = userRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new RuntimeException("Company not found"));

        PlacementRound round = new PlacementRound();
        round.setStudent(student);
        round.setCompany(company);
        round.setRoundType(roundType);
        round.setStatus(PlacementRound.RoundStatus.PENDING);
        return placementRoundRepository.save(round);
    }

    // Get placement rounds by company
    // Get placement rounds by company
    public List<RoundResponse> getRoundsByCompany(Long companyId) {
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new RuntimeException("Company not found"));
        return placementRoundRepository.findByCompany(company)
                .stream()
                .map(this::toRoundResponse)
                .collect(Collectors.toList());
    }

    // Get placement rounds by student
    public List<RoundResponse> getRoundsByStudent(Long studentId) {
        User student = userRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));
        return placementRoundRepository.findByStudent(student)
                .stream()
                .map(this::toRoundResponse)
                .collect(Collectors.toList());
    }

    private RoundResponse toRoundResponse(PlacementRound round) {
        RoundResponse dto = new RoundResponse();
        dto.setId(round.getId());
        dto.setStudentId(round.getStudent().getId());
        dto.setStudentUsername(round.getStudent().getUsername());
        dto.setStudentFullName(round.getStudent().getFullName());
        dto.setCompanyId(round.getCompany().getId());
        dto.setCompanyName(round.getCompany().getName());
        dto.setRoundType(round.getRoundType().name());
        dto.setStatus(round.getStatus().name());
        dto.setRemarks(round.getRemarks());
        dto.setCreatedAt(round.getCreatedAt());
        dto.setUpdatedAt(round.getUpdatedAt());
        return dto;
    }
}