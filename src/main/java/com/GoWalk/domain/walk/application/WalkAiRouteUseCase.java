package com.GoWalk.domain.walk.application;

import com.GoWalk.domain.walk.application.client.WalkAiClient;
import com.GoWalk.domain.walk.application.data.req.WalkAiRouteReq;
import com.GoWalk.domain.walk.application.data.response.WalkAiRouteRes;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WalkAiRouteUseCase {

    private final WalkAiClient walkAiClient;

    public WalkAiRouteRes recommendRoute(WalkAiRouteReq req) {
        return walkAiClient.recommendRoute(req);
    }
}
