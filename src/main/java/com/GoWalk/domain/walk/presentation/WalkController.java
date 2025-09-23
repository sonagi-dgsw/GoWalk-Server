package com.GoWalk.domain.walk.presentation;

import com.GoWalk.domain.walk.application.WalkRecommendationUseCase;
import com.GoWalk.domain.walk.application.data.response.WalkRecommendationRes;
import com.GoWalk.global.data.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/walk")
@RequiredArgsConstructor
public class WalkController {

    private final WalkRecommendationUseCase useCase;

    @GetMapping("/recommendation")
    public ApiResponse<WalkRecommendationRes> recommend(
            @RequestParam(defaultValue = "CALM") String mood
    ) {
        // memberId 임시로 1L
        return ApiResponse.ok(useCase.recommend(1L, mood));
    }
}
