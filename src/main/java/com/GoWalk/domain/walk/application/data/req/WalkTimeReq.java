package com.GoWalk.domain.walk.application.data.req;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record WalkTimeReq(

        @NotBlank(message = "반려동물 종류는 필수 입력값이다.")
        String petType,

        @NotBlank(message = "산책 시간은 필수 입력값이다.")
        String lastWalk,

        @NotBlank(message = "날씨는 필수 입력값이다.")
        @Pattern(
                regexp = "^(sunny|rainy)$",
                message = "날씨는 sunny, rainy 둘중 하나여야 한다"
        )
        String weather
) {}