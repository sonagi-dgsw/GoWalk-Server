package com.GoWalk.domain.member.application.data.res;

import com.GoWalk.domain.member.application.data.MockMember;

public record GetMyProfile(
    String username,
    Integer walkStreak,
    Double walkDistance
) {
  public static GetMyProfile of(MockMember member) {
    return new GetMyProfile(member.username(), 2, 1320.0);
  }
}
