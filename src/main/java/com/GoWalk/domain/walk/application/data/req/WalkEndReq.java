package com.GoWalk.domain.walk.application.data.req;

import com.GoWalk.domain.walk.application.entity.Walk;

import java.time.LocalDateTime;

public record WalkEndReq(
		Long sessionId,
		Integer distanceFeedback,
		Integer moodFeedback
) {
}
