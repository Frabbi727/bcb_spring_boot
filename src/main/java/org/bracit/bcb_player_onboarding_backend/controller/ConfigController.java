package org.bracit.bcb_player_onboarding_backend.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bracit.bcb_player_onboarding_backend.common.dto.ApiResponse;
import org.bracit.bcb_player_onboarding_backend.domain.SponsorBanner;
import org.bracit.bcb_player_onboarding_backend.service.AdminService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/config")
@RequiredArgsConstructor
public class ConfigController {

    private final AdminService adminService;

    @GetMapping("/faqs")
    public ResponseEntity<ApiResponse<List<Map<String, String>>>> getFaqs(
            @RequestParam(required = false, defaultValue = "en") String lang) {
            
        List<Map<String, String>> faqs = new ArrayList<>();
        Map<String, String> faq1 = new HashMap<>();
        
        if ("bn".equalsIgnoreCase(lang)) {
            faq1.put("question", "আমি কীভাবে নিবন্ধন করব?");
            faq1.put("answer", "ওটিপি যাচাইয়ের মাধ্যমে শুরু করুন এবং ধাপগুলো পূরণ করুন।");
        } else {
            faq1.put("question", "How do I register?");
            faq1.put("answer", "Start with verifying OTP and follow the draft steps.");
        }
        
        faqs.add(faq1);
        return ResponseEntity.ok(ApiResponse.success(faqs, "FAQs retrieved."));
    }

    @GetMapping("/banners")
    public ResponseEntity<ApiResponse<List<SponsorBanner>>> getBanners() {
        List<SponsorBanner> list = adminService.getBanners();
        return ResponseEntity.ok(ApiResponse.success(list, "Active sponsor banners retrieved."));
    }
}
