package com.GoWalk.domain.member.application.data.req;

import jakarta.validation.constraints.NotNull;

public record SignUpInReq(
		@NotNull String username,
		String email,
		@NotNull String password,
		String breed,
		Integer breed_age,
		String refreshToken
) {
}
