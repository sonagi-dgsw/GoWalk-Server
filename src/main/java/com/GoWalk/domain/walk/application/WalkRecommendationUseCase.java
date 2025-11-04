package com.GoWalk.domain.walk.application;

import com.GoWalk.domain.walk.application.data.res.WalkRecommendationRes;
import com.GoWalk.domain.walk.application.entity.WalkRoute;
import com.GoWalk.domain.walk.application.repository.WalkRouteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class WalkRecommendationUseCase {

    private final WalkRouteRepository walkRouteRepository;
    private final Random random = new Random();

    public WalkRecommendationRes recommend(Long memberId, String mood, double minRating) {
        List<WalkRoute> candidates = walkRouteRepository.findByRatingGreaterThanEqual((int) Math.ceil(minRating));

        if (candidates.isEmpty()) {
            throw new IllegalArgumentException("조건에 맞는 산책로가 없습니다.");
        }

        // 랜덤추천
        WalkRoute chosen = candidates.get(random.nextInt(candidates.size()));

        return new WalkRecommendationRes(
                chosen.getPlaces().stream().map(p -> p.getPlaceName()).toList(),
                mood,
                chosen.getRating(),
                "기분과 평점을 고려해 추천된 코스입니다."
        );
    }
}
