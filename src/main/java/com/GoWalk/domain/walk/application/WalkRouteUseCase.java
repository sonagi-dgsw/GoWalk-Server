package com.GoWalk.domain.walk.application;

import com.GoWalk.domain.walk.application.data.request.CreateWalkRouteReq;
import com.GoWalk.domain.walk.application.data.response.WalkRouteRes;
import com.GoWalk.domain.walk.application.entity.WalkRoute;
import com.GoWalk.domain.walk.application.repository.WalkRouteRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class WalkRouteUseCase {

    private final WalkRouteRepository walkRouteRepository;

    @Transactional
    public WalkRouteRes createRoute(CreateWalkRouteReq req, String currentUserId) {
        var places = req.places().stream()
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .toList();
        if (places.size() < 2) {
            throw new IllegalArgumentException("장소는 2개 이상이어야 합니다.");
        }
        String routeKey = buildRouteKey(places);
        WalkRoute route = WalkRoute.builder()
                .rating(req.rating())
                .createdAt(LocalDateTime.now())
                .createdBy(currentUserId)
                .routeKey(routeKey)
                .build();
        for (int i = 0; i < places.size(); i++) {
            route.addPlace(places.get(i), i);
        }
        WalkRoute saved = walkRouteRepository.save(route);
        return new WalkRouteRes(
                saved.getPlaces().stream().map(p -> p.getPlaceName()).toList(),
                saved.getRating()
        );
    }

    private String buildRouteKey(java.util.List<String> places) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            String joined = String.join(">", places).toLowerCase();
            byte[] digest = md.digest(joined.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (byte b : digest) sb.append(String.format("%02x", b));
            return sb.toString();
        } catch (Exception e) {
            return null;
        }
    }
}
