package com.GoWalk.domain.member.application.usecase;

import com.GoWalk.domain.member.application.data.res.RankDayRes;
import com.GoWalk.domain.member.application.data.res.RankDistanceRes;
import com.GoWalk.domain.member.application.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RankUseCase {
	private final MemberRepository memberRepository;

	public List<RankDistanceRes> getRankWithDistance() {
		return memberRepository.findAllOrderByWalkDistanceDesc();
	}

	public List<RankDayRes> getRankWithDay(){
		return memberRepository.findAllOrderByWalkDayDesc();
	}
}
