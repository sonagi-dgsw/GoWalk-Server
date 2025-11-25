package com.GoWalk.domain.member.application.data.req;

import com.GoWalk.domain.member.application.entity.PetGender;
import jakarta.validation.constraints.NotNull;

public record SignUpInReq(

		// 로그인 할 때만 필요
		@NotNull String email,
		@NotNull String password,

		 // 회원가입 시에 추가로 받는 것들
		String username,
		String breed,
		String petName,
		Integer breedAge,
		PetGender petGender,
		double petWeight
) {
}