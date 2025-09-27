package com.GoWalk.domain.member.application.data.req;

public record ReGenerateAccessToken (
		String username,
		String refreshToken
) {
}
