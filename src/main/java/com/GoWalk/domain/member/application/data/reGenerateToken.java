package com.GoWalk.domain.member.application.data;

import jakarta.servlet.http.HttpServletResponse;

public record reGenerateToken(
		String username,
		String accessToken
) {
}
