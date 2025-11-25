package com.GoWalk.global.security;

import com.GoWalk.domain.member.application.data.req.GenerateTokenReq;
import io.github.cdimascio.dotenv.Dotenv;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Slf4j
@Component
public class JwtProvider {
	private final SecretKey key;
	private final long accessToken_Validity = 3600000; // 1시간
	private final long refreshToken_Validity = 604800000; // 1주일

	public JwtProvider() {
		Dotenv dotenv = Dotenv.configure()
				.directory("./")
				.load();

		String secret = dotenv.get("JWT_SECRET");

		if (secret == null || secret.isEmpty()) {
			throw new IllegalStateException("JWT_SECRET를 확인할 수 없습니다.");
		}
		this.key = Keys.hmacShaKeyFor(secret.getBytes());
	}

	// 리프레시 토큰
	public String generateRefreshToken(GenerateTokenReq request) {
		Date now = new Date();
		Date expiration = new Date(now.getTime() + refreshToken_Validity);

		return Jwts
				.builder()
				.subject(request.email())

				.claim("tokenType", "refreshToken")

				.issuedAt(now)
				.expiration(expiration)
				.signWith(key)
				.compact();
	}

	// 엑세스 토큰
	public String generateAccessToken(GenerateTokenReq request) {
		Date now = new Date();
		Date expiration = new Date(now.getTime() + accessToken_Validity);

		return Jwts
				.builder()
				.subject(request.email())

				.claim("tokenType", "accessToken")
				.claim("role", request.role())

				.issuedAt(now)
				.expiration(expiration)
				.signWith(key)
				.compact();
	}

	public String getEmail(String token) {
		Claims claims = Jwts.parser()
				.verifyWith(key)
				.build()
				.parseSignedClaims(token)
				.getPayload();
		return claims.getSubject();
	}

	public String getRole(String token) {
		Claims claims = Jwts.parser()
				.verifyWith(key)
				.build()
				.parseSignedClaims(token)
				.getPayload();
		return claims.get("role").toString();
	}

	public String getTokenType(String token) {
		Claims claims = Jwts.parser()
				.verifyWith(key)
				.build()
				.parseSignedClaims(token)
				.getPayload();
		return claims.get("tokenType").toString();
	}

	public boolean validateToken(String token) {
		try {
			Jws<Claims> jwsClaims = Jwts.parser()
					.verifyWith(key)
					.build()
					.parseSignedClaims(token);

			Claims claims = jwsClaims.getPayload();
			Date exp = claims.getExpiration();

			if (exp.before(new Date())) {
				return false;
			}

			String tokenType = claims.get("tokenType").toString();
			if (tokenType.equalsIgnoreCase("refreshToken") || tokenType.equalsIgnoreCase("accessToken")) {
				return true;
			}

		} catch (Exception e) {
			return false;
		}
		return false;
	}

	public String resolveToken(HttpServletRequest request) {
		String jwt = request.getHeader("Authorization");
		if(jwt != null && jwt.startsWith("Bearer ")) {
			return jwt.substring(7);
		}
		return null;
	}
}

