package com.GoWalk.domain.walk.application.exception;

import com.GoWalk.global.exception.exception.ApplicationException;
import com.GoWalk.global.exception.status_code.StatusCode;

public class WalkException extends ApplicationException {
	public WalkException(StatusCode statusCode) {
		super(statusCode);
	}

	public WalkException(StatusCode statusCode, Throwable cause) {
		super(statusCode, cause);
	}

	public WalkException(StatusCode statusCode, String message) {
		super(statusCode, message);
	}
}
