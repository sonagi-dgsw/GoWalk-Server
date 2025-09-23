package com.GoWalk.domain.member.application.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Table(name = "member")
public class Member {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long Id;

	private String username; // 사용자명

	private String email; // 이메일

	private String password; // 비밀번호

	private String breed; // 반려동물 견종

	private Integer breed_age; // 반려동물 나이

	@Enumerated(EnumType.STRING)
	private Role role;
}
