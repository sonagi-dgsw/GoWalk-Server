package com.GoWalk.domain.walk.application.data.response;

import java.util.List;

public record WalkRoute(
        List<String> places, // 산책 경로에 포함된 장소들
        int rating           // 별점 (1~5)
) {}
