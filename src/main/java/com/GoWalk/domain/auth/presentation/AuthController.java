package com.GoWalk.domain.auth.presentation;

import com.GoWalk.domain.auth.exception.AuthStatusCode;
import com.GoWalk.domain.member.application.data.req.SignOutReq;
import com.GoWalk.domain.member.application.data.req.SignUpInReq;
import com.GoWalk.domain.member.application.data.res.RankRes;
import com.GoWalk.domain.member.application.data.res.SignInRes;
import com.GoWalk.domain.member.application.data.res.SignUpRes;
import com.GoWalk.domain.member.application.data.res.reGenerateAccessTokenRes;
import com.GoWalk.domain.member.application.usecase.MemberUseCase;
import com.GoWalk.domain.member.application.usecase.RankUseCase;
import com.GoWalk.domain.member.application.usecase.TokenUseCase;
import com.GoWalk.domain.member.application.exception.MemberException;
import com.GoWalk.global.data.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/")
@CrossOrigin("*")
public class AuthController {
	private final MemberUseCase memberService;
	private final TokenUseCase tokenUseCase;
	private final RankUseCase rankUseCase;

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
	public ApiResponse<?> signOut(@RequestBody SignOutReq request, HttpServletResponse response) {
		return memberService.signOut(request, response);
	}

	// 엑세스 토큰 재 발급
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

	// 랭킹
	@GetMapping("/rank")
	public List<RankRes> getRank() {
		return rankUseCase.getRank();
	}
}
