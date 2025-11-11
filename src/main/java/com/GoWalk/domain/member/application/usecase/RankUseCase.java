package com.GoWalk.domain.member.application.usecase;

import com.GoWalk.domain.member.application.data.res.RankRes;
import com.GoWalk.domain.member.application.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RankUseCase {
	private final MemberRepository memberRepository;

	public List<RankRes> getRank() {
		return memberRepository.findAllOrderByWalkDistanceDesc();
	}
}
