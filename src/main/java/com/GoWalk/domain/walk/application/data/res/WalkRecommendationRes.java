package com.GoWalk.domain.walk.application.data.res;

public record WalkRecommendationRes(
        String mood,
        String recommendedRoute,
        double rating
) {}
