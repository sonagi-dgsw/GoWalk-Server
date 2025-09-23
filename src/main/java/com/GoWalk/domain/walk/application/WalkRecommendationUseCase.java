package com.GoWalk.domain.walk.application;

import com.GoWalk.domain.walk.application.data.response.WalkRecommendationRes;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Random;

@Component
@RequiredArgsConstructor
public class WalkRecommendationUseCase {

    private final Random random = new Random();

    public WalkRecommendationRes recommend(Long memberId, String mood) {
        List<String> dummy = List.of(
                "한강공원루트 - 3km",
                "대형이 이마 반지름 루트 - 4km"
        );

        String chosen = dummy.get(random.nextInt(dummy.size()));
        return new WalkRecommendationRes(mood, chosen);
    }
}