package com.GoWalk.domain.member.application.entity;

import com.GoWalk.domain.walk.application.entity.Walk;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
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

	private String breed; // 반려견 견종

	private Integer breedAge; // 반려견 나이

	private String petName; // 반려견 이름

	private double petWeight; // 반려견 몸무게

	@Enumerated(EnumType.STRING)
	private PetGender petGender; // 반려견 성별

	@Enumerated(EnumType.STRING)
	private Role role; // 권한

	@Setter
	@NotNull
	private double walkDistance; // 누적 km

	@OneToMany(mappedBy = "member", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private List<Walk> walks = new ArrayList<>();
}
