package com.GoWalk.domain.member.application.data.req;

import jakarta.validation.constraints.NotNull;

public record SignOutReq(
		@NotNull String username
) {
}
