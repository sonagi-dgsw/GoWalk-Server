package com.GoWalk.domain.walk.application.data.res;

import java.util.List;

public record WalkRecommendationRes(
        List<String> places,
        String mood,
        double rating,
        String reason
) {}