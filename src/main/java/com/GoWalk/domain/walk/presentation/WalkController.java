package com.GoWalk.domain.walk.presentation;

import com.GoWalk.domain.walk.application.WalkRecommendationUseCase;
import com.GoWalk.domain.walk.application.WalkTimeUseCase;
import com.GoWalk.domain.walk.application.data.request.WalkTimeReq;
import com.GoWalk.domain.walk.application.data.response.WalkRecommendationRes;
import com.GoWalk.domain.walk.application.data.response.WalkTimeRes;
import com.GoWalk.global.data.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/walk")
@RequiredArgsConstructor
public class WalkController {

    private final WalkRecommendationUseCase walkRecommendationUseCase;
    private final WalkTimeUseCase walkTimeUseCase;

    @GetMapping("/recommendation")
    public ApiResponse<WalkRecommendationRes> recommend(
            @RequestParam(defaultValue = "CALM") String mood,
            @RequestParam(defaultValue = "0") double minRating
    ) {
        return ApiResponse.ok(walkRecommendationUseCase.recommend(1L, mood, minRating));
    }

    @PostMapping("/time")
    public ApiResponse<WalkTimeRes> recommendTime(@Valid @RequestBody WalkTimeReq request) {
        return ApiResponse.ok(walkTimeUseCase.recommendTime(request));
    }
}
