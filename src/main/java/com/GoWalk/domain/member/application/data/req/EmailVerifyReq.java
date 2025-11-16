package com.GoWalk.domain.member.application.data.req;

public record EmailVerifyReq(
		String email,
		String authNum
) {
}
