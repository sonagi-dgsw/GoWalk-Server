package com.GoWalk.domain.walk.application.repository;

import com.GoWalk.domain.walk.application.entity.WalkRoutePlace;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface WalkRoutePlaceRepository extends JpaRepository<WalkRoutePlace, Long> {

    interface HotPlaceCount {
        String getPlaceName();
        Long getCnt();
    }

    @Query("""
           select wp.placeName as placeName, count(wp) as cnt
           from WalkRoutePlace wp
           where wp.route.rating <= :maxRating
           group by wp.placeName
           order by cnt desc
           """)
    List<HotPlaceCount> countHotPlacesByMax(@Param("maxRating") int maxRating);
}
