package com.GoWalk.global.security;

import com.GoWalk.domain.member.application.entity.Member;
import com.GoWalk.domain.member.application.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberDetailsService implements UserDetailsService {
	private final MemberRepository memberRepository;

	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Member member = memberRepository.findByUsername(username).orElseThrow(()
				-> new UsernameNotFoundException("유저정보를 찾을 수 없습니다."));

		return new MemberDetails(member);
	}
}
