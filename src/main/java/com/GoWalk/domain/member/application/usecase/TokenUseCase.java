package com.GoWalk.domain.member.application.usecase;

import com.GoWalk.domain.auth.exception.AuthStatusCode;
import com.GoWalk.domain.member.application.data.req.GenerateTokenReq;
import com.GoWalk.domain.member.application.data.res.reGenerateAccessTokenRes;
import com.GoWalk.domain.member.application.entity.Member;
import com.GoWalk.domain.member.application.exception.MemberException;
import com.GoWalk.domain.member.application.exception.MemberStatusCode;
import com.GoWalk.domain.member.application.repository.MemberRepository;
import com.GoWalk.global.config.RedisConfig;
import com.GoWalk.global.data.ApiResponse;
import com.GoWalk.global.security.JwtProvider;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
@Slf4j
public class TokenUseCase {
	private final JwtProvider jwtProvider;
	private final RedisConfig redisConfig;
	private final MemberRepository memberRepository;

	public String generateAccessToken(
			GenerateTokenReq token,
			String email,
			HttpServletResponse response) {

		String accessToken = jwtProvider.generateAccessToken(token);
		redisConfig.redisTemplate().opsForValue().set("accessToken:" + email, accessToken, 1, TimeUnit.HOURS);

		Cookie accessCookie = new Cookie("accessToken", accessToken);
		accessCookie.setPath("/");
		accessCookie.setHttpOnly(false);
		accessCookie.setMaxAge(60 * 60); // 1시간
		response.addCookie(accessCookie);
		return accessToken;
	}

	public String generateRefreshToken(
			GenerateTokenReq tokenReq,
			String email,
			HttpServletResponse response) {

		String refreshToken = jwtProvider.generateRefreshToken(tokenReq);
		redisConfig.redisTemplate().opsForValue().set("refreshToken:" + email, refreshToken, 7, TimeUnit.DAYS);

		Cookie refreshCookie = new Cookie("refreshToken", refreshToken);
		refreshCookie.setPath("/");
		refreshCookie.setHttpOnly(false); // 임시로 false입니다.
		refreshCookie.setMaxAge(60 * 60 * 24 * 7); // 7일
		response.addCookie(refreshCookie);
		return refreshToken;
	}

	public ApiResponse<reGenerateAccessTokenRes> reGenerateAccessToken(
			HttpServletRequest request,
			HttpServletResponse response) {

		String refreshToken = Arrays.stream(Optional.ofNullable(request.getCookies()).orElseThrow(()
				-> new MemberException(MemberStatusCode.INVALID_JWT)))
				.filter(cookie -> "refreshToken".equals(cookie.getName()))
				.map(Cookie::getValue).findFirst().orElseThrow(()
				-> new MemberException(MemberStatusCode.INVALID_JWT));

		if (!jwtProvider.validateToken(refreshToken)) {
			throw new MemberException(MemberStatusCode.INVALID_JWT);
		}
		String email = jwtProvider.getEmail(refreshToken);

		ValueOperations<String, String> valueOperations = redisConfig.redisTemplate().opsForValue();
		String savedRefreshToken = valueOperations.get("refreshToken:" + email);

		if (savedRefreshToken != null && savedRefreshToken.equals(refreshToken)) {
			Member member = memberRepository.findByEmail(email).orElseThrow(()
			-> new MemberException(MemberStatusCode.MEMBER_CANNOT_FOUND));

			GenerateTokenReq reGenerateAccessToken = new GenerateTokenReq(
					member.getEmail(),
					member.getRole()
			);
			String newAccessToken = jwtProvider.generateAccessToken(reGenerateAccessToken);

			Cookie accessCookie = new Cookie("accessToken", newAccessToken);
			accessCookie.setPath("/");
			accessCookie.setHttpOnly(false);
			accessCookie.setMaxAge(60 * 60); // 1시간
			response.addCookie(accessCookie);
			return ApiResponse.ok(reGenerateAccessTokenRes.of(newAccessToken));
		}
		throw new MemberException(MemberStatusCode.INVALID_JWT);
	}

	public void deleteTokens(
			HttpServletRequest request,
			HttpServletResponse response) {
		ValueOperations<String, String> valueOperations = redisConfig.redisTemplate().opsForValue();

		String email = getMemberFromAccessToken(request).getEmail();

		String savedRefreshToken = valueOperations.get("refreshToken:" + email);
		String savedAccessToken = valueOperations.get("accessToken:" + email);

		if (savedRefreshToken == null || savedAccessToken == null) {
			throw new MemberException(AuthStatusCode.ALREADY_LOGGED_OUT);
		} else {
			// 엑세스 토큰 만료(쿠키)
			Cookie accessCookie = new Cookie("accessToken", null);
			accessCookie.setPath("/");
			accessCookie.setHttpOnly(false);
			accessCookie.setMaxAge(0); // 즉시 만료
			response.addCookie(accessCookie);

			// 리프레시 토큰 만료(쿠키)
			Cookie refreshCookie = new Cookie("refreshToken", null);
			refreshCookie.setPath("/");
			refreshCookie.setHttpOnly(true);
			refreshCookie.setMaxAge(0); // 즉시 만료
			response.addCookie(refreshCookie);

			// Redis에서 삭제
			redisConfig.redisTemplate().delete("accessToken:" + email);
			redisConfig.redisTemplate().delete("refreshToken:" + email);
		}
	}

	// 토큰에서 사용자명 추출
	public Member getMemberFromAccessToken(HttpServletRequest request) {
		String accessToken = Arrays.stream(Optional.ofNullable(request.getCookies()).orElseThrow(()
						-> new MemberException(MemberStatusCode.INVALID_JWT)))
				.filter(cookie -> "accessToken".equals(cookie.getName()))
				.map(Cookie::getValue).findFirst().orElseThrow(()
						-> new MemberException(MemberStatusCode.INVALID_JWT));

		if (!jwtProvider.validateToken(accessToken)) {
			throw new MemberException(MemberStatusCode.INVALID_JWT);
		}
		String userId = jwtProvider.getEmail(accessToken);

		return memberRepository.findByEmail(userId).orElseThrow(()
				-> new MemberException(MemberStatusCode.MEMBER_CANNOT_FOUND));
	}
}