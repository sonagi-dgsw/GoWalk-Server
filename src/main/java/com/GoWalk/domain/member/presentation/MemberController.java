package com.GoWalk.domain.member.presentation;

import com.GoWalk.domain.member.application.MemberUseCase;
import com.GoWalk.domain.member.application.data.res.GetMyInfoRes;
import com.GoWalk.domain.member.application.data.res.GetMyProfile;
import com.GoWalk.global.data.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/members")
public class MemberController {
  private final MemberUseCase memberUseCase;

  @GetMapping("/me")
  public ApiResponse<GetMyInfoRes> me() {
    return ApiResponse.ok(memberUseCase.getMyInfo());
  }

  @GetMapping("/profile")
  public ApiResponse<GetMyProfile> profile() {
    return ApiResponse.ok(memberUseCase.getMyProfile());
  }
}
