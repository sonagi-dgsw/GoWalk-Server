package com.GoWalk.domain.walk.application.data.req;

import jakarta.validation.constraints.NotNull;

public record WalkStartReq(
		@NotNull(message = "산책로가 지정되지 않았습니다.")
		Long trailId
) {
}
