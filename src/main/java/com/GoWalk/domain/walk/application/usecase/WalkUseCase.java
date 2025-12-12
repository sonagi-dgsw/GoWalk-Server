package com.GoWalk.domain.walk.application.usecase;

import com.GoWalk.domain.member.application.entity.Member;
import com.GoWalk.domain.member.application.exception.MemberException;
import com.GoWalk.domain.member.application.exception.MemberStatusCode;
import com.GoWalk.domain.member.application.repository.MemberRepository;
import com.GoWalk.domain.walk.application.data.req.WalkEndReq;
import com.GoWalk.domain.walk.application.data.req.WalkQuitReq;
import com.GoWalk.domain.walk.application.data.req.WalkStartReq;
import com.GoWalk.domain.walk.application.data.res.WalkEndRes;
import com.GoWalk.domain.walk.application.data.res.WalkQuitRes;
import com.GoWalk.domain.walk.application.data.res.WalkStartRes;
import com.GoWalk.domain.walk.application.entity.Walk;
import com.GoWalk.domain.walk.application.entity.WalkStatus;
import com.GoWalk.domain.walk.application.exception.WalkException;
import com.GoWalk.domain.walk.application.exception.WalkStatusCode;
import com.GoWalk.domain.walk.application.repository.WalkRepository;
import com.GoWalk.global.data.ApiResponse;
import com.GoWalk.global.security.JwtProvider;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class WalkUseCase {
	private final WalkRepository walkRepository;
	private final MemberRepository memberRepository;
	private final JwtProvider jwtProvider;

	// 산책 시작
	@Transactional
	public ApiResponse<WalkStartRes> walkStart(WalkStartReq request, HttpServletRequest http) {

		Member member = getEmailFromToken(http);

		if (walkRepository.existsByMemberIdAndStatus(member.getId(), WalkStatus.STARTED)) {
			throw new WalkException(WalkStatusCode.WALK_ALREADY_STARTED);
		}

		// 산책 중인 세션이 없다면 새 산책 세션을 만들어서 반환
		Walk walk = Walk.builder()
				.trailId(request.trailId())
				.startTime(LocalDateTime.now())
				.endTime(null)
				.status(WalkStatus.STARTED)
				.member(member)
				.distanceFeedback(null)
				.moodFeedback(null)
				.build();
		walkRepository.save(walk);

		return ApiResponse.ok(WalkStartRes.of(walk));
	}

	// 산책 완료
	@Transactional
	public ApiResponse<WalkEndRes> walkEnd(WalkEndReq request, HttpServletRequest http) {

		Member member = getEmailFromToken(http);

		Walk walk = walkRepository.findById(request.sessionId()).orElseThrow(()
		-> new WalkException(WalkStatusCode.SESSION_CANNOT_FOUND));

		// 남의 세션에 접근 불가
		if (!walk.getMember().getId().equals(member.getId())) {
			throw new WalkException(WalkStatusCode.SESSION_CANNOT_FOUND);
		}

		// 이미 완료된 세션이면 예외
		if (WalkStatus.FINISHED.equals(walk.getStatus())) {
			throw new WalkException(WalkStatusCode.WALK_ALREADY_FINISHED);
		}

		walk.setStatus(WalkStatus.FINISHED); // 상태 변경
		walk.setEndTime(LocalDateTime.now()); // 산책 완료 시간
		walk.setDistance(request.distance()); // 산책 거리
		walk.setWalkDay((calculateDays(walk.getStartTime(), walk.getEndTime()))); // 산책 일 수 계산

		walk.setDistanceFeedback(request.distanceFeedback()); // 피드백 저장
		walk.setMoodFeedback(request.moodFeedback()); // 피드백 저장
		walkRepository.save(walk);

		member.setWalkDistance(member.getWalkDistance() + walk.getDistance());
		memberRepository.save(member);

		return ApiResponse.ok(new WalkEndRes());
	}

	// 산책 중도 포기
	@Transactional
	public ApiResponse<WalkQuitRes> walkQuit(WalkQuitReq request, HttpServletRequest http) {

		Member member = getEmailFromToken(http);

		Walk walk = walkRepository.findById(request.sessionId()).orElseThrow(()
		-> new WalkException(WalkStatusCode.SESSION_CANNOT_FOUND));

		// 남의 세션에 접근 불가
		if (!walk.getMember().getId().equals(member.getId())) {
			throw new WalkException(WalkStatusCode.SESSION_CANNOT_FOUND);
		}

		if (WalkStatus.FINISHED.equals(walk.getStatus())) {
			throw new WalkException(WalkStatusCode.WALK_ALREADY_FINISHED);
		}

		walkRepository.delete(walk);

		return ApiResponse.ok(new WalkQuitRes());
	}

	// 토큰에서 이메일 추출 및 멤버 추출
	public Member getEmailFromToken(HttpServletRequest http) {
		String token = jwtProvider.resolveToken(http);
		if (token == null || !jwtProvider.validateToken(token)) {
			throw new WalkException(MemberStatusCode.INVALID_JWT);
		}

		String email = jwtProvider.getEmail(token);

		Member member = memberRepository.findByEmail(email).orElseThrow(()
				-> new MemberException(MemberStatusCode.MEMBER_CANNOT_FOUND));

		if (member == null || member.getEmail() == null) {
			throw new MemberException(MemberStatusCode.MEMBER_CANNOT_FOUND);
		}

		return memberRepository.findByEmail(member.getEmail()).orElseThrow(()
				-> new MemberException(MemberStatusCode.MEMBER_CANNOT_FOUND));
	}

	// 시간(일 수) 계산
	private Double calculateDays(LocalDateTime start, LocalDateTime end) {
		if (start == null || end == null) {
			return 0.0;
		}

		long seconds = java.time.Duration.between(start, end).toSeconds();
		double days = (double)  seconds / 86400; // 1일
		return Math.round(days * 100000) / 100000.0; // 소수점 다섯째 자리까지 반올림
	}
}