package com.GoWalk.domain.walk.application.repository;

import com.GoWalk.domain.walk.application.entity.WalkRoute;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WalkRouteRepository extends JpaRepository<WalkRoute, Long> {
    List<WalkRoute> findByRatingGreaterThanEqual(int rating);
}