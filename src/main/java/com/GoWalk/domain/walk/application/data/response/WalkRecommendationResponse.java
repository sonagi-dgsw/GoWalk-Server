package com.GoWalk.domain.walk.application.data.response;

public record WalkRecommendationResponse(
        String mood,
        String recommendedRoute
) {}
