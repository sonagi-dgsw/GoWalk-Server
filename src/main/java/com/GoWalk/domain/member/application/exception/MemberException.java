package com.GoWalk.domain.member.application.exception;

import com.GoWalk.global.exception.exception.ApplicationException;
import com.GoWalk.global.exception.status_code.StatusCode;

public class MemberException extends ApplicationException {

  public MemberException(StatusCode statusCode) {
    super(statusCode);
  }

  public MemberException(StatusCode statusCode, Throwable cause) {
    super(statusCode, cause);
  }

  public MemberException(StatusCode statusCode, String message) {
    super(statusCode, message);
  }
}
