package com.GoWalk.domain.member.application.usecase;

import com.GoWalk.domain.auth.exception.AuthException;
import com.GoWalk.domain.auth.exception.AuthStatusCode;
import com.GoWalk.domain.member.application.data.req.GenerateTokenReq;
import com.GoWalk.domain.member.application.data.req.SignOutReq;
import com.GoWalk.domain.member.application.data.req.SignUpInReq;
import com.GoWalk.domain.member.application.data.res.*;
import com.GoWalk.domain.member.application.entity.Member;
import com.GoWalk.domain.member.application.entity.Role;
import com.GoWalk.domain.member.application.exception.MemberException;
import com.GoWalk.domain.member.application.exception.MemberStatusCode;
import com.GoWalk.domain.member.application.repository.MemberRepository;
import com.GoWalk.global.data.ApiResponse;
import com.GoWalk.global.security.JwtProvider;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberUseCase {
	private final MemberRepository memberRepository;
	private final PasswordEncoder passwordEncoder;
	private final TokenUseCase tokenUseCase;
	private final JwtProvider jwtProvider;

	public ApiResponse<GetMyInfoRes> getMyInfo(HttpServletRequest request) {
			Member member = getMemberFromAccessToken(request);
			return ApiResponse.ok(GetMyInfoRes.of(member));
	}

	public ApiResponse<GetMyProfile> getMyProfile(HttpServletRequest request) {
		Member member = getMemberFromAccessToken(request);
		return ApiResponse.ok(GetMyProfile.of(member));
	}

	// 회원가입
	public ApiResponse<SignUpRes> signUp(SignUpInReq request) {
		if (memberRepository.existsByUsername(request.username())) {
			throw new AuthException(AuthStatusCode.USERNAME_ALREADY_EXIST);
		}
		if (memberRepository.existsByEmail(request.email())) {
			throw new AuthException(AuthStatusCode.EMAIL_ALREADY_EXIST);
		}

		String rawPassword = request.password();
		if (!isValidPassword(rawPassword)) {
			throw new AuthException(AuthStatusCode.PASSWORD_IS_WEAK);
		}

		Member member = Member.builder()
				.username(request.username())
				.email(request.email())
				.password(passwordEncoder.encode(rawPassword))
				.breed(request.breed())
				.breedAge(request.breedAge())
				.role(Role.ROLE_USER)
				.walkDistance(0.00)
				.build();
		memberRepository.save(member);
		return ApiResponse.ok(SignUpRes.of(member));
	}

	// 로그인 + 토큰 발급
	public ApiResponse<SignInRes> signIn(SignUpInReq request, HttpServletResponse response) {
		Member member = memberRepository.findByUsername(request.username()).orElseThrow(()
				-> new IllegalArgumentException("사용자명 혹은 비밀번호가 잘못되었습니다."));
		if (!passwordEncoder.matches(request.password(), member.getPassword())) {
			throw new IllegalArgumentException("사용자명 혹은 비밀번호가 잘못되었습니다.");
		}
		String userId = request.username();

		GenerateTokenReq genAccessTokenReq = new GenerateTokenReq(
				member.getUsername(),
				member.getRole()
		);

		GenerateTokenReq genRefreshTokenReq = new GenerateTokenReq(
				member.getUsername(),
				member.getRole()
		);

		// 토큰 생성
		String accessToken = tokenUseCase.generateAccessToken(genAccessTokenReq, userId, response);
		String refreshToken = tokenUseCase.generateRefreshToken(genRefreshTokenReq, userId, response);
		return ApiResponse.ok(SignInRes.of(accessToken, refreshToken));
	}

	// 로그아웃
	public ApiResponse<?> signOut(SignOutReq request,  HttpServletResponse response) {
		tokenUseCase.deleteTokens(request, response);
		return ApiResponse.ok("로그아웃이 정상적으로 처리되었습니다.");
	}

	// 비밀번호 검증식
	private boolean isValidPassword(String password) {
		if (password == null || password.length() < 8) { // 만약  비밀번호가 공백이거나 8자 미만인지 확인
			return false;
		}
		boolean hasUpperCase = password.matches(".*[A-Z].*"); // 영어 대문자중 하나라도 포함되어 있는지 확인
		boolean hasLowerCase = password.matches(".*[a-z].*"); // 영어 소문자중 하나라도 포함되어 있는지 확인
		boolean hasDigit = password.matches(".*\\d.*"); // 숫자가 하나라도 있는지 확인
		boolean hasSpecial = password.matches(".*[!@#$%^&*(),.?\":{}|<>].*"); // 특수문자가 하나라도 있는지 확인
		return hasUpperCase && hasLowerCase && hasDigit && hasSpecial;
	}

	// 토큰에서 사용자명 추출
	private Member getMemberFromAccessToken(HttpServletRequest request) {
		String accessToken = Arrays.stream(Optional.ofNullable(request.getCookies()).orElseThrow(()
						-> new MemberException(MemberStatusCode.INVALID_JWT)))
				.filter(cookie -> "accessToken".equals(cookie.getName()))
				.map(Cookie::getValue).findFirst().orElseThrow(()
						-> new MemberException(MemberStatusCode.INVALID_JWT));

		if (!jwtProvider.validateToken(accessToken)) {
			throw new MemberException(MemberStatusCode.INVALID_JWT);
		}
		String userId = jwtProvider.getUsername(accessToken);

		return memberRepository.findByUsername(userId).orElseThrow(()
				-> new MemberException(MemberStatusCode.MEMBER_CANNOT_FOUND));
	}
}