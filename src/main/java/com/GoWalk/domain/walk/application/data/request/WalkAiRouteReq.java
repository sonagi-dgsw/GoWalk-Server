package com.GoWalk.domain.walk.application.data.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class WalkAiRouteReq {

    @NotNull @Valid
    private Point start;

    @NotNull
    @Min(100) @Max(10000)
    private Integer distanceLimitM;

    @NotBlank
    private String theme;

    @Getter @Setter
    @NoArgsConstructor @AllArgsConstructor
    @Builder
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class Point {
        @NotNull private Double lat;
        @NotNull private Double lng;
    }
}
