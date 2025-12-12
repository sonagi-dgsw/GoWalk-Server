package com.GoWalk.domain.walk.application.data.res;

import com.GoWalk.domain.walk.application.entity.Walk;

import java.time.LocalDateTime;

public record WalkStartRes(
		Long sessionId,
		Long trailID,
		LocalDateTime startTime
) {
	public static WalkStartRes of(Walk walk) {
		return new WalkStartRes(walk.getId(), walk.getTrailId(), walk.getStartTime());
	}
}