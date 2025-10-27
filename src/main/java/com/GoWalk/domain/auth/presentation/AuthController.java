package com.GoWalk.domain.auth.presentation;

import com.GoWalk.domain.auth.exception.AuthStatusCode;
import com.GoWalk.domain.member.application.data.req.ReGenerateAccessToken;
import com.GoWalk.domain.member.application.data.req.SignOutReq;
import com.GoWalk.domain.member.application.data.req.SignUpInReq;
import com.GoWalk.domain.member.application.data.res.SignInRes;
import com.GoWalk.domain.member.application.data.res.SignUpRes;
import com.GoWalk.domain.member.application.data.res.reGenerateAccessTokenRes;
import com.GoWalk.domain.member.application.usecase.MemberUseCase;
import com.GoWalk.domain.member.application.usecase.TokenUseCase;
import com.GoWalk.domain.member.application.exception.MemberException;
import com.GoWalk.global.data.ApiResponse;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/auth/")
@CrossOrigin("*")
public class AuthController {
	private final MemberUseCase memberService;
	private final TokenUseCase tokenUseCase;

	// 회원가입
	@PostMapping("/signup")
	public ApiResponse<SignUpRes> signUp(@RequestBody SignUpInReq request) {
		return memberService.signUp(request);
	}

	// 로그인
	@PostMapping("/signin")
	public ApiResponse<SignInRes> signIn(@RequestBody SignUpInReq request, HttpServletResponse response) {
		return memberService.signIn(request, response);
	}

	// 로그아웃
	@PostMapping("/signout")
	public ApiResponse<?> signOut(@RequestBody SignOutReq request) {
		return memberService.signOut(request);
	}

	@PostMapping("/refresh")
	public ApiResponse<reGenerateAccessTokenRes> reGenerateToken(@RequestBody ReGenerateAccessToken request, HttpServletResponse response) {
		try {
			return tokenUseCase.reGenerateAccessToken(request, response);
		} catch (MemberException e) {
			return ApiResponse.error(AuthStatusCode.INVALID_JWT);
		} catch (Exception e) {
			return ApiResponse.error(AuthStatusCode.CANNOT_GENERATE_TOKEN);
		}
	}
}
