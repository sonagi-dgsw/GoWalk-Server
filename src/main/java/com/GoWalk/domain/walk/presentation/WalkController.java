package com.GoWalk.domain.walk.presentation;

import com.GoWalk.domain.walk.application.HotPlaceUseCase;
import com.GoWalk.domain.walk.application.WalkRecommendationUseCase;
import com.GoWalk.domain.walk.application.WalkRouteUseCase;
import com.GoWalk.domain.walk.application.WalkTimeUseCase;
import com.GoWalk.domain.walk.application.data.req.WalkTimeReq;
import com.GoWalk.domain.walk.application.data.request.CreateWalkRouteReq;
import com.GoWalk.domain.walk.application.data.res.WalkEndRes;
import com.GoWalk.domain.walk.application.data.res.WalkQuitRes;
import com.GoWalk.domain.walk.application.data.res.WalkRecommendationRes;
import com.GoWalk.domain.walk.application.data.res.WalkStartRes;
import com.GoWalk.domain.walk.application.data.res.WalkTimeRes;
import com.GoWalk.domain.walk.application.data.response.HotPlaceRes;
import com.GoWalk.domain.walk.application.data.response.WalkRouteRes;
import com.GoWalk.domain.walk.application.client.WalkAiClient;
import com.GoWalk.domain.walk.application.data.req.WalkEndReq;
import com.GoWalk.domain.walk.application.data.req.WalkQuitReq;
import com.GoWalk.domain.walk.application.data.request.WalkAiRouteReq;
import com.GoWalk.domain.walk.application.data.response.*;
import com.GoWalk.domain.walk.application.usecase.WalkUseCase;
import com.GoWalk.domain.walk.application.data.req.WalkStartReq;
import com.GoWalk.global.data.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import java.util.List;
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
    private final WalkUseCase walkUseCase;

    private final WalkAiClient walkAiClient;

    @GetMapping("/recommendation")
    public ApiResponse<WalkRecommendationRes> recommend(
            @RequestParam(defaultValue = "CALM") String mood,
            @RequestParam(defaultValue = "0") double minRating
    ) {
        return ApiResponse.ok(walkRecommendationUseCase.recommend(1L, mood, minRating));
    };

    // 산책 시작
    @PostMapping("/start")
    public ApiResponse<WalkStartRes> walkStart(@Valid @RequestBody WalkStartReq request, HttpServletRequest http) {
        return walkUseCase.walkStart(request, http);
    }

    @PostMapping("/time")
    public ApiResponse<WalkTimeRes> recommendTime(@Valid @RequestBody WalkTimeReq request) {
        return ApiResponse.ok(walkTimeUseCase.recommendTime(request));
    }

    // 산책완료
    @PostMapping("/finish")
    public ApiResponse<WalkEndRes> walkEnd(@Valid @RequestBody WalkEndReq request, HttpServletRequest http) {
        return walkUseCase.walkEnd(request, http);
    }

    // maxRating 사용
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

    // 산책중도 포기
    @DeleteMapping("/quit")
    public ApiResponse<WalkQuitRes> walkQuit(@Valid @RequestBody WalkQuitReq request, HttpServletRequest http) {
        return walkUseCase.walkQuit(request, http);
    }
}
