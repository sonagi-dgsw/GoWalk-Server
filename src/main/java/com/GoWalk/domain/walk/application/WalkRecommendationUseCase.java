package com.GoWalk.domain.walk.application;

import com.GoWalk.domain.walk.application.data.response.WalkRecommendationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Random;

@Component
@RequiredArgsConstructor
public class WalkRecommendationUseCase {

    private final Random random = new Random();

    public WalkRecommendationResponse recommend(Long memberId, String mood) {
        List<String> dummy = List.of(
                "한강공원루트 - 3km",
                "대형이 이마 반지름 루트 - 4km"
        );

        String chosen = dummy.get(random.nextInt(dummy.size()));
        return new WalkRecommendationResponse(mood, chosen);
    }
}