package com.GoWalk.domain.member.application;

import com.GoWalk.domain.member.application.data.MockMember;
import com.GoWalk.domain.member.application.data.req.GenerateToken;
import com.GoWalk.domain.member.application.data.req.SignOutReq;
import com.GoWalk.domain.member.application.data.req.SignUpInReq;
import com.GoWalk.domain.member.application.entity.Member;
import com.GoWalk.domain.member.application.entity.Role;
import com.GoWalk.domain.member.application.repository.MemberRepository;
import com.GoWalk.global.config.RedisConfig;
import com.GoWalk.global.security.JwtProvider;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberUseCase {
	private final MemberRepository memberRepository;
	private final PasswordEncoder passwordEncoder;
	private final TokenUseCase tokenUseCase;

	public MockMember getMyInfo() {
		return new MockMember("gorani", "hello world");
	}

	// 회원가입
	public ResponseEntity<?> signUp(SignUpInReq request) {

		if (memberRepository.existsByUsername(request.username())) {
			return ResponseEntity.badRequest().body(Map.of("username_error", "중복된 사용자명입니다."));
		}
		if (memberRepository.existsByEmail(request.email())) {
			return ResponseEntity.badRequest().body(Map.of("email_error", "해당 이메일로 이미 가입된 계정이 있습니다."));
		}
		String rawPassword = request.password();
		if(!isValidPassword(rawPassword)){
			return ResponseEntity.badRequest().body(Map.of("password_error", "비밀번호는 8자 이상이며, 영어 대소문자, 숫자, 특수문자를 각각 1개 이상 포함해야 합니다."));
		}

		Member member = Member.builder()
				.username(request.username())
				.email(request.email())
				.password(passwordEncoder.encode(rawPassword))
				.breed(request.breed())
				.breed_age(request.breed_age())
				.role(Role.ROLE_USER)
				.build();
		memberRepository.save(member);
		return ResponseEntity.ok(member);
	}

	// 로그인 + 토큰 발급
	public ResponseEntity<?> signIn(SignUpInReq request, HttpServletResponse response) {
		Member member = memberRepository.findByUsername(request.username()).orElseThrow(()
				-> new IllegalArgumentException("사용자명 혹은 비밀번호가 잘못되었습니다."));
		if (!passwordEncoder.matches(request.password(), member.getPassword())) {
			throw new IllegalArgumentException("사용자명 혹은 비밀번호가 잘못되었습니다.");
		}
		String userId = request.username();

		GenerateToken genAccessTokenReq = new GenerateToken(
				member.getUsername(),
				member.getRole()
		);

		GenerateToken genRefreshTokenReq = new GenerateToken(
				member.getUsername(),
				member.getRole()
		);

		// Redis 저장용
		String accessToken = tokenUseCase.generateAccessToken(genAccessTokenReq, userId, response);
		String refreshToken = tokenUseCase.generateRefreshToken(genRefreshTokenReq, userId, response);
		return ResponseEntity.ok(Map.of("access_token", accessToken, "refresh_token", refreshToken));
	}



	// 로그아웃
	public void signOut(SignOutReq request) {
		String userId = request.username();
		tokenUseCase.deleteTokens(userId);
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
}