package com.GoWalk.domain.walk.presentation;

import com.GoWalk.domain.walk.application.WalkRecommendationUseCase;
import com.GoWalk.domain.walk.application.data.response.WalkRecommendationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/walk")
@RequiredArgsConstructor
public class WalkController {

    private final WalkRecommendationUseCase useCase;

    @GetMapping("/recommendation")
    public ResponseEntity<WalkRecommendationResponse> recommend(
            @RequestParam(defaultValue = "CALM") String mood
    ) {
        // memberId 임시로 1L
        return ResponseEntity.ok(useCase.recommend(1L, mood));
    }
}
