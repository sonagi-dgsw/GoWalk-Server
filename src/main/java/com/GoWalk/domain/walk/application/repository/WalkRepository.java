package com.GoWalk.domain.walk.application.repository;

import com.GoWalk.domain.walk.application.entity.Walk;
import com.GoWalk.domain.walk.application.entity.WalkStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WalkRepository extends JpaRepository<Walk,Long> {
	Optional<Walk> findById(Long id); // 산책 아이디로 찾음

	boolean existsByMemberIdAndStatus(Long memberId, WalkStatus walkStatus);
	boolean existsByIdAndStatus(Long sessionId, WalkStatus walkStatus);
}
