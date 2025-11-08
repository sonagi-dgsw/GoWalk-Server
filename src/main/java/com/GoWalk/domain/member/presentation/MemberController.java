package com.GoWalk.domain.member.presentation;

import com.GoWalk.domain.member.application.data.res.GetMyProfile;
import com.GoWalk.domain.member.application.usecase.MemberUseCase;
import com.GoWalk.domain.member.application.data.res.GetMyInfoRes;
import com.GoWalk.global.data.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/members")
@Slf4j
public class MemberController {
  private final MemberUseCase memberUseCase;

  @GetMapping("/me")
  public ApiResponse<GetMyInfoRes> me(HttpServletRequest request) {
    return memberUseCase.getMyInfo(request);
  }

  @GetMapping("/profile")
  public ApiResponse<GetMyProfile> profile(HttpServletRequest request) {
    return memberUseCase.getMyProfile(request);
  }
}
