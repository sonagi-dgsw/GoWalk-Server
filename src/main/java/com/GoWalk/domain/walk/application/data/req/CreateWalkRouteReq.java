package com.GoWalk.domain.walk.application.data.request;

import jakarta.validation.constraints.*;
import java.util.List;

public record CreateWalkRouteReq(
        @NotEmpty(message = "장소 리스트는 최소 2개 이상이어야 합니다.")
        @Size(min = 2, message = "장소는 2개 이상 입력하세요.")
        List<@NotBlank(message = "장소명은 비어 있을 수 없습니다.") String> places,

        @Min(value = 1, message = "별점은 1 이상이어야 합니다.")
        @Max(value = 5, message = "별점은 5 이하여야 합니다.")
        int rating
) {}
