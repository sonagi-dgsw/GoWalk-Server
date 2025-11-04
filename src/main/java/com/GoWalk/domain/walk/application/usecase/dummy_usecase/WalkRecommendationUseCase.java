package com.GoWalk.domain.walk.application.usecase.dummy_usecase;

import com.GoWalk.domain.walk.application.data.res.WalkRecommendationRes;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Random;


/** AI로 처리 예정 **/

//@Component
//@RequiredArgsConstructor
//public class WalkRecommendationUseCase {
//
//    private final Random random = new Random();
//
//    public WalkRecommendationRes recommend(Long memberId, String mood, double minRating) {
//        List<WalkDummy> dummy = List.of(
//                new WalkDummy("한강공원루트 - 3km", 4.5),
//                new WalkDummy("대형이 이마 반지름 루트 - 4km", 3.2),
//                new WalkDummy("아파트 공원 루트 - 1km", 2.5),
//                new WalkDummy("BlaBlaBla 루트 - 2km", 4.8)
//        );
//
//        List<WalkDummy> filtered = dummy.stream()
//                .filter(w -> w.rating() >= minRating)
//                .toList();
//
//        if (filtered.isEmpty()) {
//            throw new IllegalArgumentException("조건에 맞는 산책로가 없습니다.");
//        }
//
//        WalkDummy chosen = filtered.get(random.nextInt(filtered.size()));
//        return new WalkRecommendationRes(mood, chosen.name(), chosen.rating());
//    }
//
//    private record WalkDummy(String name, double rating) {}
//}
