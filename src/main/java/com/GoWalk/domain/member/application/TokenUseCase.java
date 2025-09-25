package com.GoWalk.domain.member.application;

import com.GoWalk.domain.auth.exception.AuthStatusCode;
import com.GoWalk.domain.member.application.data.req.GenerateToken;
import com.GoWalk.domain.member.application.data.req.SignUpInReq;
import com.GoWalk.domain.member.application.entity.Member;
import com.GoWalk.domain.member.application.exception.MemberException;
import com.GoWalk.domain.member.application.repository.MemberRepository;
import com.GoWalk.global.config.RedisConfig;
import com.GoWalk.global.security.JwtProvider;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class TokenUseCase {
	private final JwtProvider jwtProvider;
	private final RedisConfig redisConfig;
	private final MemberRepository memberRepository;

	public String generateAccessToken(GenerateToken token, String userId, HttpServletResponse response) {
		String accessToken = jwtProvider.generateAccessToken(token);
		redisConfig.redisTemplate().opsForValue().set("accessToken:" + userId, accessToken, 20, TimeUnit.SECONDS);

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

		Cookie refreshCookie = new Cookie("refreshToken", refreshToken);
		refreshCookie.setPath("/");
		refreshCookie.setHttpOnly(true);
		refreshCookie.setMaxAge(60 * 60 * 24 * 7); // 7일
		response.addCookie(refreshCookie);
		return refreshToken;
	}

	public boolean validateRefreshToken(String userId, String refreshToken) {
		ValueOperations<String, String> valueOperations = redisConfig.redisTemplate().opsForValue();
		String savedRefreshToken = valueOperations.get("refreshToken:" + userId);
		return savedRefreshToken != null && savedRefreshToken.equals(refreshToken);
	}

	// 토큰 재발급
	// 엑세스 토큰이 살아있어도 요청 날리면 재발급 해주긴 함;;
	public ResponseEntity<?> reGenerateToken(SignUpInReq request, HttpServletResponse response) {
		ValueOperations<String, String> valueOperations = redisConfig.redisTemplate().opsForValue();
		String userId = request.username();
		String savedRefreshToken = valueOperations.get("refreshToken:" + userId);


		if (!this.validateRefreshToken(userId, savedRefreshToken)) {
			throw new MemberException(AuthStatusCode.INVALID_JWT);
		}

		Member member = memberRepository.findByUsername(userId).orElseThrow(()
				-> new MemberException(AuthStatusCode.INVALID_JWT));

		GenerateToken reGenerateAccessToken = new GenerateToken(
				member.getUsername(),
				member.getRole());

		String newAccessToken = this.generateAccessToken(reGenerateAccessToken, userId, response);
		return ResponseEntity.ok(Map.of("access_token", newAccessToken));
	}

	public void deleteTokens(String userId) {
		redisConfig.redisTemplate().delete("accessToken:" + userId);
		redisConfig.redisTemplate().delete("refreshToken:" + userId);
	}
}
