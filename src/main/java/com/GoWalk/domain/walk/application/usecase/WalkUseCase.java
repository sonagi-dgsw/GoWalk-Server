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
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class WalkUseCase {
	private final WalkRepository walkRepository;
	private final MemberRepository memberRepository;

	// 산책 시작
	public ApiResponse<WalkStartRes> walkStart(WalkStartReq request) {
		if (walkRepository.existsByMemberIdAndStatus(request.memberId(), WalkStatus.STARTED)) {
			throw new WalkException(WalkStatusCode.WALK_ALREADY_STARTED); // 이미 산책중인 세션이 있으면 새 산책을 시작할 수 없도록 예외 던짐
		}
		Member member = memberRepository.findById(request.memberId()).orElseThrow(()
				-> new MemberException(MemberStatusCode.MEMBER_CANNOT_FOUND));

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
	public ApiResponse<WalkEndRes> walkEnd(WalkEndReq request) {
		Walk walk = walkRepository.findById(request.sessionId()).orElseThrow(()
		-> new WalkException(WalkStatusCode.SESSION_CANNOT_FOUND));

		Member member = walk.getMember();

		if (walkRepository.existsByIdAndStatus(request.sessionId(), WalkStatus.FINISHED)) {
			throw new WalkException(WalkStatusCode.WALK_ALREADY_FINISHED);
		}

		walk.setStatus(WalkStatus.FINISHED);
		walk.setEndTime(LocalDateTime.now());
		walk.setDistanceFeedback(request.distanceFeedback());
		walk.setMoodFeedback(request.moodFeedback());
		member.setWalkDistance(member.getWalkDistance() + walk.getDistance());
		walkRepository.save(walk);
		memberRepository.save(member);

		return ApiResponse.ok(new WalkEndRes());
	}

	// 산책 중도 포기
	public ApiResponse<WalkQuitRes> walkQuit(WalkQuitReq request) {
		Walk walk = walkRepository.findById(request.sessionId()).orElseThrow(()
		-> new WalkException(WalkStatusCode.SESSION_CANNOT_FOUND));

		if (WalkStatus.FINISHED.equals(walk.getStatus())) {
			throw new WalkException(WalkStatusCode.WALK_ALREADY_FINISHED);
		}

		walkRepository.delete(walk);

		return ApiResponse.ok(new WalkQuitRes());
	}
}
