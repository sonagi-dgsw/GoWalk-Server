package com.GoWalk.domain.member.application.data.res;

public record reGenerateAccessTokenRes(
		String accessToken
) {
	public static reGenerateAccessTokenRes of(String accessToken) {
		return new reGenerateAccessTokenRes(accessToken);
	}
}
