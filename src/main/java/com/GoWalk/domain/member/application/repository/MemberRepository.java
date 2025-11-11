package com.GoWalk.domain.member.application.repository;

import com.GoWalk.domain.member.application.data.res.RankRes;
import com.GoWalk.domain.member.application.entity.Member;
import io.lettuce.core.dynamic.annotation.Param;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member,Long> {
	Optional<Member> findByUsername(String username);

	Optional<Member> findByEmail(String email);

	boolean existsByUsername(String username);

	boolean existsByEmail(String email);

	boolean existsById(Long id);

	/** 랭킹 관련 **/
	@Transactional
	@Modifying
	@Query("SELECT m.username, m.walkDistance FROM Member m ORDER BY m.walkDistance DESC")
	List<RankRes> findAllOrderByWalkDistanceDesc();
}
