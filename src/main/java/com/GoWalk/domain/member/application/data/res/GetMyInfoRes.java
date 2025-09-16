package com.GoWalk.domain.member.application.data.res;

import jakarta.validation.constraints.NotNull;

public record GetMyInfoRes(
    @NotNull String username,
    @NotNull String message
) {}