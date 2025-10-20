package com.GoWalk.domain.member.application.data.res;

import com.GoWalk.domain.member.application.entity.Member;

public record SignUpRes(
		Long id,
		String username,
		String email,
		String breed,
		Integer breed_age
) {
	public static SignUpRes of(Member member) {
		return new SignUpRes(member.getId(), member.getUsername(), member.getEmail(), member.getBreed(), member.getBreed_age());
	}
}