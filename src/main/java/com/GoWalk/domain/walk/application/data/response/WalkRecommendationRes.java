package com.GoWalk.domain.walk.application.data.response;

public record WalkRecommendationRes(
        String mood,
        String recommendedRoute,
        double rating
) {}
