package com.GoWalk.domain.walk.application;

import com.GoWalk.domain.walk.application.data.response.HotPlaceRes;
import com.GoWalk.domain.walk.application.repository.HotPlaceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HotPlaceUseCase {

    private final HotPlaceRepository hotPlaceRepository;
    public List<HotPlaceRes> getHotPlaces(int top, int maxRating) {
        var rows = hotPlaceRepository.countHotPlacesByMax(maxRating);
        if (rows.isEmpty()) return List.of();

        return rows.stream()
                .limit(top)
                .map(r -> new HotPlaceRes(r.getPlaceName(), r.getCnt()))
                .toList();
    }
}
