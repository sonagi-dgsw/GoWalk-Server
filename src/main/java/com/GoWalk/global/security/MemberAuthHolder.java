package com.GoWalk.global.security;

import com.GoWalk.domain.member.application.entity.Member;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class MemberAuthHolder {
	public Member current() {
		return ((MemberDetails) SecurityContextHolder
				.getContext()
				.getAuthentication()
				.getPrincipal())
				.getMember();
	}
}
