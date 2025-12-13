package com.GoWalk.domain.auth.presentation;

import com.GoWalk.domain.auth.exception.AuthStatusCode;
import com.GoWalk.domain.member.application.data.req.EmailSendReq;
import com.GoWalk.domain.member.application.data.req.EmailVerifyReq;
import com.GoWalk.domain.member.application.data.req.SignUpInReq;
import com.GoWalk.domain.member.application.data.res.*;
import com.GoWalk.domain.member.application.usecase.EmailUseCase;
import com.GoWalk.domain.member.application.usecase.MemberUseCase;
import com.GoWalk.domain.member.application.usecase.TokenUseCase;
import com.GoWalk.domain.member.application.exception.MemberException;
import com.GoWalk.global.data.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/")
@CrossOrigin("*")
public class AuthController {
	private final MemberUseCase memberService;
	private final TokenUseCase tokenUseCase;
	private final EmailUseCase emailUseCase;

	// 회원가입
	@PostMapping("auth/signup")
	public ApiResponse<SignUpRes> signUp(@RequestBody SignUpInReq request) {
		return memberService.signUp(request);
	}

	// 로그인
	@PostMapping("auth/signin")
	public ApiResponse<SignInRes> signIn(@RequestBody SignUpInReq request, HttpServletResponse response) {
		return memberService.signIn(request, response);
	}

	// 로그아웃
	@PostMapping("auth/signout")
	public ApiResponse<?> signOut(HttpServletRequest http, HttpServletResponse response) {
		return memberService.signOut(http, response);
	}

	// 인증 이메일 전송
	@PostMapping("email/send")
	public void sendEmail(@Valid @RequestBody EmailSendReq request) {
		emailUseCase.sendEmail(request.email());
	}

	// 이메일 인증 확인
	@PostMapping("email/verify")
	public ApiResponse<EmailRes> verifyEmail(@RequestBody EmailVerifyReq request) {
		return emailUseCase.verifyEmail(request);
	}

	// 엑세스 토큰 재발급
	@PostMapping("auth/refresh")
	public ApiResponse<reGenerateAccessTokenRes> reGenerateToken(HttpServletRequest request, HttpServletResponse response) {
		try {
			return tokenUseCase.reGenerateAccessToken(request, response);
		} catch (MemberException e) {
			return ApiResponse.error(AuthStatusCode.INVALID_JWT);
		} catch (Exception e) {
			return ApiResponse.error(AuthStatusCode.CANNOT_GENERATE_TOKEN);
		}
	}
}
