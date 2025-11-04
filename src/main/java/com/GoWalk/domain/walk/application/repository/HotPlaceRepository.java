package com.GoWalk.domain.walk.application.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class HotPlaceRepository {

    private final WalkRoutePlaceRepository placeRepository;

    public List<WalkRoutePlaceRepository.HotPlaceCount> countHotPlacesByMax(int maxRating) {
        return placeRepository.countHotPlacesByMax(maxRating);
    }
}