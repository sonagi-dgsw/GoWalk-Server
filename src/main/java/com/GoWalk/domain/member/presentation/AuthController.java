package com.GoWalk.domain.member.presentation;

import com.GoWalk.domain.member.application.data.req.SignOutReq;
import com.GoWalk.domain.member.application.data.req.SignUpInReq;
import com.GoWalk.domain.member.application.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/auth/")
@CrossOrigin("*")
public class AuthController {
	private final MemberService memberService;

	// 회원가입
	@PostMapping("/signup")
	public ResponseEntity<?> signUp(@RequestBody SignUpInReq request) {
		return memberService.signUp(request);
	}

	// 로그인
	@PostMapping("/signin")
	public ResponseEntity<?> signIn(@RequestBody SignUpInReq request) {
		return memberService.signIn(request);
	}

	// 로그아웃
	@PostMapping("/signout")
	public ResponseEntity<?> signOut(@RequestBody SignOutReq request) {
		memberService.signOut(request);
		return ResponseEntity.ok(Map.of("message", "정상적으로 로그아웃 되었습니다."));
	}
}
