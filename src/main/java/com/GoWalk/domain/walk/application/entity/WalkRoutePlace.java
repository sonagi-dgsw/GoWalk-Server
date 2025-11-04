package com.GoWalk.domain.walk.application.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor @Builder
@Table(name = "walk_route_place", indexes = {
        @Index(name = "idx_place_name", columnList = "placeName"),
        @Index(name = "idx_route_id", columnList = "route_id")
})
public class WalkRoutePlace {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String placeName;

    @Column(nullable = false)
    private int stepOrder;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "route_id")
    private WalkRoute route;
}
