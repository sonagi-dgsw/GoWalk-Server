package com.GoWalk.domain.member.application.usecase;

import com.GoWalk.domain.auth.exception.AuthStatusCode;
import com.GoWalk.domain.member.application.data.req.GenerateTokenReq;
import com.GoWalk.domain.member.application.data.req.SignOutReq;
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

	public String generateAccessToken(GenerateTokenReq token, String userId, HttpServletResponse response) {
		String accessToken = jwtProvider.generateAccessToken(token);
		redisConfig.redisTemplate().opsForValue().set("accessToken:" + userId, accessToken, 1, TimeUnit.HOURS);

		Cookie accessCookie = new Cookie("accessToken", accessToken);
		accessCookie.setPath("/");
		accessCookie.setHttpOnly(false);
		accessCookie.setMaxAge(60 * 60); // 1시간
		response.addCookie(accessCookie);
		return accessToken;
	}

	public String generateRefreshToken(GenerateTokenReq tokenReq, String userId, HttpServletResponse response) {
		String refreshToken = jwtProvider.generateRefreshToken(tokenReq);
		redisConfig.redisTemplate().opsForValue().set("refreshToken:" + userId, refreshToken, 7, TimeUnit.DAYS);

		Cookie refreshCookie = new Cookie("refreshToken", refreshToken);
		refreshCookie.setPath("/");
		refreshCookie.setHttpOnly(true);
		refreshCookie.setMaxAge(60 * 60 * 24 * 7); // 7일
		response.addCookie(refreshCookie);
		return refreshToken;
	}

	public ApiResponse<reGenerateAccessTokenRes> reGenerateAccessToken(HttpServletRequest request, HttpServletResponse response) {
		String refreshToken = Arrays.stream(Optional.ofNullable(request.getCookies()).orElseThrow(()
				-> new MemberException(MemberStatusCode.INVALID_JWT)))
				.filter(cookie -> "refreshToken".equals(cookie.getName()))
				.map(Cookie::getValue).findFirst().orElseThrow(()
				-> new MemberException(MemberStatusCode.INVALID_JWT));

		if (!jwtProvider.validateToken(refreshToken)) {
			throw new MemberException(MemberStatusCode.INVALID_JWT);
		}
		String userId = jwtProvider.getUsername(refreshToken);

		ValueOperations<String, String> valueOperations = redisConfig.redisTemplate().opsForValue();
		String savedRefreshToken = valueOperations.get("refreshToken:" + userId);

		if (savedRefreshToken != null && savedRefreshToken.equals(refreshToken)) {
			Member member = memberRepository.findByUsername(userId).orElseThrow(()
			-> new MemberException(MemberStatusCode.MEMBER_CANNOT_FOUND));

			GenerateTokenReq reGenerateAccessToken = new GenerateTokenReq(
					member.getUsername(),
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

	public void deleteTokens(SignOutReq request) {
		ValueOperations<String, String> valueOperations = redisConfig.redisTemplate().opsForValue();
		String userId = request.username();
		String savedRefreshToken = valueOperations.get("refreshToken:" + userId);
		String savedAccessToken = valueOperations.get("accessToken:" + userId);

		if (savedRefreshToken == null || savedAccessToken == null) {
			throw new MemberException(AuthStatusCode.ALREADY_LOGGED_OUT);
		} else {
			redisConfig.redisTemplate().delete("accessToken:" + userId);
			redisConfig.redisTemplate().delete("refreshToken:" + userId);
		}
	}
}