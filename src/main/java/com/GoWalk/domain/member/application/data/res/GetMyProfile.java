package com.GoWalk.domain.member.application.data.res;

import com.GoWalk.domain.member.application.entity.Member;
import jakarta.validation.constraints.NotNull;

public record GetMyProfile(
    String username,
    Double walkDistance,
    Double walkDay
) {
  public static GetMyProfile of(Member member, double walkDay) {
    return new GetMyProfile(member.getUsername(), member.getWalkDistance(), walkDay);
  }
}
