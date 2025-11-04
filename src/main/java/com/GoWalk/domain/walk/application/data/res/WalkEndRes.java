package com.GoWalk.domain.walk.application.data.res;

import com.GoWalk.domain.walk.application.entity.Walk;

public record WalkEndRes(
		boolean success,
		String message
) {
	public WalkEndRes() {
		this(true, "산책이 성공적으로 완료되었습니다.");
	}
}