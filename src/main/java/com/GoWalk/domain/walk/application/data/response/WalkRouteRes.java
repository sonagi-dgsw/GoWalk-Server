package com.GoWalk.domain.walk.application.data.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class WalkRoute {
    private List<String> places;  // 경로에 포함된 장소들
    private int rating;           // 경로 별점
}
