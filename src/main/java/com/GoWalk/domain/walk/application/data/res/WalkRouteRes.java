package com.GoWalk.domain.walk.application.data.res;

import java.util.List;

public record WalkRouteRes(
		List<String> places, // 경로에 포함된 장소들
		int rating // 경로의 별점
) {}