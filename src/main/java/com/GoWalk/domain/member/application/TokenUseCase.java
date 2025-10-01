package com.GoWalk.domain.member.application;

import com.GoWalk.domain.auth.exception.AuthStatusCode;
import com.GoWalk.domain.member.application.data.req.GenerateTokenReq;
import com.GoWalk.domain.member.application.data.req.ReGenerateAccessToken;
import com.GoWalk.domain.member.application.data.req.SignOutReq;
import com.GoWalk.domain.member.application.data.req.SignUpInReq;
import com.GoWalk.domain.member.application.entity.Member;
import com.GoWalk.domain.member.application.exception.MemberException;
import com.GoWalk.domain.member.application.repository.MemberRepository;
import com.GoWalk.global.config.RedisConfig;
import com.GoWalk.global.data.ApiResponse;
import com.GoWalk.global.security.JwtProvider;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.Map;
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

	// 쿠키에서 리프레시 토큰 추출
	private String getRefreshTokenFromCookie(HttpServletRequest httpRequest) {
		if (httpRequest.getCookies() != null) {
			for (Cookie cookie : httpRequest.getCookies()) {
				if (cookie.getName().equals("refreshToken")) {
					String value =  cookie.getValue();
					if (value != null && !value.isEmpty()) {
						return value;
					}
				}
			}
		}
		throw new MemberException(AuthStatusCode.INVALID_JWT);
	}

	public boolean validateRefreshToken(ReGenerateAccessToken request) {
		ValueOperations<String, String> valueOperations = redisConfig.redisTemplate().opsForValue();
		String userId = request.username();
		String clientRefreshToken = request.refreshToken();

		String savedRefreshToken = valueOperations.get("refreshToken:" + userId);

		if (savedRefreshToken != null && !savedRefreshToken.isEmpty()) {
			return clientRefreshToken.equals(savedRefreshToken);
		}
		return false;
	}

	public ResponseEntity<?> reGenerateAccessToken(ReGenerateAccessToken request, HttpServletResponse response) {
		String userId = request.username();

		if (validateRefreshToken(request)) {
			Member member = memberRepository.findByUsername(userId).orElseThrow(()
					-> new MemberException(AuthStatusCode.INVALID_JWT));

			GenerateTokenReq reGenerateAccessToken = new GenerateTokenReq(
					member.getUsername(),
					member.getRole()
			);
			String newAccessToken = this.generateAccessToken(reGenerateAccessToken, userId, response);
			return ResponseEntity.ok(Map.of("accessToken", newAccessToken));
		}
		throw new MemberException(AuthStatusCode.INVALID_JWT);
	}

	public void deleteTokens(SignOutReq request) {
		String userId = request.username();
		redisConfig.redisTemplate().delete("accessToken:" + userId);
		redisConfig.redisTemplate().delete("refreshToken:" + userId);
	}
}