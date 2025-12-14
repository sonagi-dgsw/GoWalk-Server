package com.GoWalk.domain.walk.application.data.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;

import java.util.List;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class WalkAiRouteRes {

    private String theme;
    private Double totalEstimatedDistanceM;
    private List<Waypoint> waypoints;
    private List<String> narration;

    @Getter @Setter
    @NoArgsConstructor @AllArgsConstructor
    @Builder
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class Waypoint {
        private Integer order;
        private String name;
        private Double lat;
        private Double lng;
    }
}
