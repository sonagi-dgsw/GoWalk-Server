package com.GoWalk.domain.member.application.data.res;

public record SignInRes(
		String accessToken,
		String refreshToken
) {
	public static SignInRes of(String accessToken, String refreshToken) {
		return new SignInRes(accessToken, refreshToken);
	}
}
