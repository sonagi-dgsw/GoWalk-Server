package com.GoWalk.domain.walk.application.data.response;

import java.util.List;

public record WalkRouteRes(
        List<String> places,
        int rating
) {}