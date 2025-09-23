package com.GoWalk.domain.member.application.service;

import com.GoWalk.domain.member.application.data.req.GenerateToken;
import com.GoWalk.global.config.RedisConfig;
import com.GoWalk.global.security.JwtProvider;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class TokenService {
	private final JwtProvider jwtProvider;
	private final RedisConfig redisConfig;

	public String generateAccessToken(GenerateToken tokenReq, String userId, HttpServletResponse response) {
		String accessToken = jwtProvider.generateAccessToken(tokenReq);
		redisConfig.redisTemplate().opsForValue().set("accessToken:" + userId, accessToken, 1, TimeUnit.HOURS);

		Cookie accessCookie = new Cookie("accessToken", accessToken);
		accessCookie.setPath("/");
		accessCookie.setHttpOnly(false);
		accessCookie.setMaxAge(60 * 60); // 1시간
		response.addCookie(accessCookie);
		return accessToken;
	}

	public String generateRefreshToken(GenerateToken tokenReq, String userId, HttpServletResponse response) {
		String refreshToken = jwtProvider.generateRefreshToken(tokenReq);
		redisConfig.redisTemplate().opsForValue().set("refreshToken:" + userId, refreshToken, 7, TimeUnit.DAYS);

		Cookie accessCookie = new Cookie("refreshToken", refreshToken);
		accessCookie.setPath("/");
		accessCookie.setHttpOnly(true);
		accessCookie.setMaxAge(60 * 60 * 24 * 7); // 7일
		response.addCookie(accessCookie);
		return refreshToken;
	}

	public void deleteTokens(String userId) {
		redisConfig.redisTemplate().delete("accessToken:" + userId);
		redisConfig.redisTemplate().delete("refreshToken:" + userId);
	}

	public boolean validateRefreshToken(String userId, String refreshToken) {
		ValueOperations<String, String> valueOperations = redisConfig.redisTemplate().opsForValue();
		String savedRefreshToken = valueOperations.get("refreshToken:" + userId);
		return savedRefreshToken != null && savedRefreshToken.equals(refreshToken);
	}
}
