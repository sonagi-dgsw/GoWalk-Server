package com.GoWalk.domain.walk.application.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor @Builder
@Table(
        name = "walk_route",
        indexes = {
                @Index(name = "idx_walk_route_created_by", columnList = "createdBy"),
                @Index(name = "idx_walk_route_created_at", columnList = "createdAt")
        }
)
public class WalkRoute {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private int rating;

    @OneToMany(mappedBy = "route", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("stepOrder ASC")
    @Builder.Default
    private List<WalkRoutePlace> places = new ArrayList<>();

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(length = 64)
    private String createdBy;

    @Column(length = 128)
    private String routeKey;

    public void addPlace(String placeName, int stepOrder) {
        WalkRoutePlace p = WalkRoutePlace.builder()
                .route(this)
                .placeName(placeName)
                .stepOrder(stepOrder)
                .build();
        places.add(p);
    }
}
