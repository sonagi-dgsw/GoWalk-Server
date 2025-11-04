package com.GoWalk.domain.walk.application.data.req;

import jakarta.validation.constraints.NotNull;

public record WalkStartReq(
		@NotNull(message = "사용자 정보를 확인할 수 없습니다.")
		Long memberId,
		@NotNull(message = "산책로가 지정되지 않았습니다.")
		Long trailId
) {
}
