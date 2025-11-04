package com.GoWalk.domain.walk.application.entity;

import com.GoWalk.domain.member.application.entity.Member;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Table(name = "walk")
public class Walk {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 산책 세션 ID

	private Long trailId; // 산책로 ID

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id")
	private Member member; // 사용자 ID

	private LocalDateTime startTime; // 산책 시작 시간

	@Setter
	private LocalDateTime endTime; // 산책 종료 시간

	@Setter
	@Enumerated(EnumType.STRING)
	private WalkStatus status; // 산책 상태(시작, 종료, 중도포기)

	@Setter
	private Integer distanceFeedback; // 산책 거리 피드백

	@Setter
	private Integer moodFeedback; // 산책 분위기 피드백
}
