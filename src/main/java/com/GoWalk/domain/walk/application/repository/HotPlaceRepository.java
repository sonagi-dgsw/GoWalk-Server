package com.GoWalk.domain.walk.application.repository;

import com.GoWalk.domain.walk.application.data.res.WalkRouteRes;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class HotPlaceRepository {

    public List<WalkRouteRes> loadGoodWalks() {
        return List.of(
                new WalkRouteRes(List.of("한강공원", "뚝섬유원지", "카페거리"), 5),
                new WalkRouteRes(List.of("한강공원", "시청앞광장", "카페거리"), 4),
                new WalkRouteRes(List.of("한강공원", "뚝섬유원지", "전망대"), 3),
                new WalkRouteRes(List.of("올림픽공원", "한강공원", "카페거리"), 5),
                new WalkRouteRes(List.of("북서울꿈의숲", "뚝섬유원지", "전망대"), 2),
                new WalkRouteRes(List.of("올림픽공원", "전망대", "카페거리"), 4),
                new WalkRouteRes(List.of("서울숲", "뚝섬유원지", "한강공원"), 5)
        );
    }
}
