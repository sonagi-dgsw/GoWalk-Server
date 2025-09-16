package com.GoWalk.domain.member.presentation;

import com.GoWalk.domain.member.application.MemberUseCase;
import com.GoWalk.domain.member.application.data.MockMember;
import com.GoWalk.global.data.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/members")
public class MemberController {
  private final MemberUseCase memberUseCase;

  @GetMapping("/me")
  public ApiResponse<MockMember> me() {
    return ApiResponse.ok(memberUseCase.getMyInfo());
  }
}
