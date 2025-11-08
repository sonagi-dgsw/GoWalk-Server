package com.GoWalk.domain.walk.application;

import com.GoWalk.domain.walk.application.data.res.HotPlaceRes;
import com.GoWalk.domain.walk.application.data.res.WalkRouteRes;
import com.GoWalk.domain.walk.application.repository.HotPlaceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HotPlaceUseCase {

    private final HotPlaceRepository hotPlaceRepository;

    public List<HotPlaceRes> getHotPlaces(int top, int minRating) {
        List<WalkRouteRes> goodWalks = hotPlaceRepository.loadGoodWalks();

        // minRating 이상인 경로만 필터링
        var filtered = goodWalks.stream()
                .filter(r -> r.rating() >= minRating)
                .toList();

        if (filtered.isEmpty()) {
            return List.of(); // 조건에 맞는거 없으면 빈거 반환
        }

        // 장소별 등장 횟수 집계
        Map<String, Long> placeCount = filtered.stream()
                .flatMap(r -> r.places().stream())
                .collect(Collectors.groupingBy(p -> p, Collectors.counting()));

        // 등장 횟수 내림차순 정렬 & top 개수 만큼 반환
        return placeCount.entrySet().stream()
                .sorted((a, b) -> Long.compare(b.getValue(), a.getValue()))
                .limit(top)
                .map(e -> new HotPlaceRes(e.getKey(), e.getValue()))
                .toList();
    }
}
