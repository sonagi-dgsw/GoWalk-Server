package com.GoWalk.domain.member.application.repository;

import com.GoWalk.domain.member.application.data.res.RankDistanceRes;
import com.GoWalk.domain.member.application.data.res.RankDayRes;
import com.GoWalk.domain.member.application.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
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
	@Query("select new com.GoWalk.domain.member.application.data.res.RankDistanceRes(m.username, m.walkDistance)" +
			"from Member m ORDER BY m.walkDistance desc")
	List<RankDistanceRes> findAllOrderByWalkDistanceDesc();

	@Query("select new com.GoWalk.domain.member.application.data.res.RankDayRes(w.member.username, sum(w.walkDay))" +
			"from Walk w group by w.member.username order by sum(w.walkDay) desc")
	List<RankDayRes> findAllOrderByWalkDayDesc();
}
