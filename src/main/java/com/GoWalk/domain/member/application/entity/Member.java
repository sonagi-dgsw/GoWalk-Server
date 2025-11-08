package com.GoWalk.domain.member.application.entity;

import com.GoWalk.domain.walk.application.entity.Walk;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Table(name = "member")
public class Member {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String username; // 사용자명

	private String email; // 이메일

	private String password; // 비밀번호

	private String breed; // 반려동물 견종

	private Integer breedAge; // 반려동물 나이

	@Enumerated(EnumType.STRING)
	private Role role; // 권한

	private Integer rank; // 랭킹(null 허용)

	private Integer walkStreak; // 연속 산책 일수

	private Double walkDistance; // 누적 km

	@OneToMany(mappedBy = "member", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private List<Walk> walks = new ArrayList<>();
}
