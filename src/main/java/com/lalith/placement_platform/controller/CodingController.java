package com.lalith.placement_platform.controller;

import com.lalith.placement_platform.service.CodingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/coding")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class CodingController {

    private final CodingService codingService;

    @GetMapping("/recommend")
    public ResponseEntity<Map<String, String>> getRecommendations(
            @RequestParam String targetCompany,
            @RequestParam(defaultValue = "Easy to Medium") String difficulty) {
        return ResponseEntity.ok(codingService.getRecommendations(targetCompany, difficulty));
    }
}