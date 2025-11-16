package com.GoWalk.domain.walk.application.exception;

import com.GoWalk.global.exception.status_code.StatusCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum WalkStatusCode implements StatusCode {
	SESSION_ALREADY_EXIST("SESSION_ALREADY_EXIST", "해당 세션이 이미 존재합니다.", HttpStatus.UNAUTHORIZED),
	WALK_ALREADY_STARTED("WALK_ALREADY_STARTED", "이미 산책이 진행 중 입니다.", HttpStatus.UNAUTHORIZED),
	WALK_ALREADY_FINISHED("WALK_ALREADY_FINISHED", "이미 완료된 산책입니다.", HttpStatus.BAD_REQUEST),
	SESSION_CANNOT_FOUND("SESSION_CANNOT_FOUND", "세션을 찾을 수 없습니다.", HttpStatus.BAD_REQUEST);

	private final String code;
	private final String message;
	private final HttpStatus httpStatus;
}
