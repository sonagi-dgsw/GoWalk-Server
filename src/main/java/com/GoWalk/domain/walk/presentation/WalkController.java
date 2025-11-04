package com.GoWalk.domain.walk.presentation;

import com.GoWalk.domain.walk.application.HotPlaceUseCase;
import com.GoWalk.domain.walk.application.WalkRecommendationUseCase;
import com.GoWalk.domain.walk.application.WalkRouteUseCase;
import com.GoWalk.domain.walk.application.WalkTimeUseCase;
import com.GoWalk.domain.walk.application.client.WalkAiClient;
import com.GoWalk.domain.walk.application.data.request.CreateWalkRouteReq;
import com.GoWalk.domain.walk.application.data.request.WalkAiRouteReq;
import com.GoWalk.domain.walk.application.data.request.WalkTimeReq;
import com.GoWalk.domain.walk.application.data.response.*;
import com.GoWalk.global.data.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/walk")
@RequiredArgsConstructor
@Slf4j
public class WalkController {

    private final WalkRecommendationUseCase walkRecommendationUseCase;
    private final WalkTimeUseCase walkTimeUseCase;
    private final HotPlaceUseCase hotPlaceUseCase;
    private final WalkRouteUseCase walkRouteUseCase;

    private final WalkAiClient walkAiClient;

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

    @GetMapping("/hotplace")
    public ApiResponse<List<HotPlaceRes>> getHotPlaces(
            @RequestParam(defaultValue = "3") int top,
            @RequestParam(defaultValue = "5") int maxRating
    ) {
        return ApiResponse.ok(hotPlaceUseCase.getHotPlaces(top, maxRating));
    }

    @PostMapping("/routes")
    public ApiResponse<WalkRouteRes> createRoute(
            @Valid @RequestBody CreateWalkRouteReq req,
            @AuthenticationPrincipal(expression = "username") String currentUserId,
            @RequestHeader(value = "X-User-Id", required = false) String fallbackUserId
    ) {
        String userId = (currentUserId != null && !currentUserId.isBlank())
                ? currentUserId
                : (fallbackUserId != null ? fallbackUserId : "anonymous");

        return ApiResponse.ok(walkRouteUseCase.createRoute(req, userId));
    }

    @PostMapping("/ai-route")
    public ApiResponse<WalkAiRouteRes> aiRoute(@Valid @RequestBody WalkAiRouteReq req) {
        return ApiResponse.ok(walkAiClient.recommendRoute(req));
    }
}
