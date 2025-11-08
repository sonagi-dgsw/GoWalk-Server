package com.GoWalk.domain.member.application.data.res;

import com.GoWalk.domain.member.application.entity.Member;
import jakarta.validation.constraints.NotNull;

public record GetMyProfile(
    @NotNull String username,
    @NotNull Integer rank,
    @NotNull Integer walkStreak,
    @NotNull Double walkDistance
) {
  public static GetMyProfile of(Member member) {
    return new GetMyProfile(member.getUsername(), member.getRank(), member.getWalkStreak(), member.getWalkDistance());
  }
}
