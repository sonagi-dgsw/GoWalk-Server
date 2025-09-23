package com.GoWalk.domain.member.application.data.res;

import com.GoWalk.domain.member.application.data.MockMember;
import jakarta.validation.constraints.NotNull;

public record GetMyInfoRes(
    @NotNull String username,
    @NotNull String message
) {
  public static GetMyInfoRes of(MockMember member) {
    return new GetMyInfoRes(member.username(), member.message());
  }
}