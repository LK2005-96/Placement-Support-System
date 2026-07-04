package com.lalith.placement_platform.controller;

import com.lalith.placement_platform.entity.PlacementRound;
import com.lalith.placement_platform.entity.User;
import com.lalith.placement_platform.repository.PlacementRoundRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rounds")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class StudentRoundController {

    private final PlacementRoundRepository placementRoundRepository;

    @GetMapping("/my")
    public ResponseEntity<List<PlacementRound>> getMyRounds(
            @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(placementRoundRepository.findByStudent(user));
    }
}