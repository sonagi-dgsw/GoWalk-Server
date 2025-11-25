package com.GoWalk.domain.member.presentation;

import com.GoWalk.domain.member.application.data.res.GetMyProfile;
import com.GoWalk.domain.member.application.data.res.RankDayRes;
import com.GoWalk.domain.member.application.data.res.RankDistanceRes;
import com.GoWalk.domain.member.application.usecase.MemberUseCase;
import com.GoWalk.domain.member.application.data.res.GetMyInfoRes;
import com.GoWalk.domain.member.application.usecase.RankUseCase;
import com.GoWalk.global.data.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/members")
@Slf4j
public class MemberController {
  private final MemberUseCase memberUseCase;
  private final RankUseCase rankUseCase;

  @GetMapping("/me")
  public ApiResponse<GetMyInfoRes> me(HttpServletRequest request) {
    return memberUseCase.getMyInfo(request);
  }

  @GetMapping("/profile")
  public ApiResponse<GetMyProfile> profile(HttpServletRequest request) {
    return memberUseCase.getMyProfile(request);
  }

  // 거리로 랭킹
  @GetMapping("/rank/distance")
  public List<RankDistanceRes> getRankWithDistance() {
    return rankUseCase.getRankWithDistance();
  }

  // 시간으로 랭킹
  @GetMapping("/rank/time")
  public List<RankDayRes> getRankWithTime() {
    return rankUseCase.getRankWithDay();
  }
}
