package com.GoWalk.domain.walk.application.data.req;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record WalkEndReq(
		@NotNull
		Long sessionId,

		@NotNull
		Double distance,

		@NotNull @Min(1) @Max(5)
		Integer distanceFeedback,

		@NotNull @Min(1) @Max(5)
		Integer moodFeedback
) {
}
