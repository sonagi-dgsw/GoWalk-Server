package com.GoWalk.domain.walk.application.data.res;

public record WalkQuitRes(
		boolean success,
		String message
) {
	public WalkQuitRes() {
		this(true, "산책을 포기했습니다.");
	}
}