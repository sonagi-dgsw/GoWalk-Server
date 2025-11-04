package com.GoWalk.domain.member.application.exception;

import com.GoWalk.global.exception.status_code.StatusCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum MemberStatusCode implements StatusCode {
  INVALID_JWT("INVALID_JWT", "유효하지 않은 JWT입니다.", HttpStatus.UNAUTHORIZED),
  INVALID_CREDENTIALS("INVALID_CREDENTIALS", "아이디 또는 비밀번호가 일치하지 않습니다.", HttpStatus.UNAUTHORIZED),
  ACCOUNT_LOCKED("ACCOUNT_LOCKED", "계정이 잠겨 있습니다.", HttpStatus.UNAUTHORIZED),
  ACCOUNT_DISABLED("ACCOUNT_DISABLED", "계정이 비활성화되었습니다.", HttpStatus.UNAUTHORIZED),
  MEMBER_CANNOT_FOUND("MEMBER_CANNOT_FOUND", "사용자를 찾을 수 없습니다.", HttpStatus.BAD_REQUEST);

  private final String code;
  private final String message;
  private final HttpStatus httpStatus;
}
