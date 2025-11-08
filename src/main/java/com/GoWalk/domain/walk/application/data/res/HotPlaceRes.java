package com.GoWalk.domain.walk.application.data.res;

public record HotPlaceRes(
		String place, // 장소명
		Long satisfaction // 공통으로 포함된 횟수
) {}