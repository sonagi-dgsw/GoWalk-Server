package com.GoWalk.domain.walk.application.client;

import com.GoWalk.domain.walk.application.data.request.WalkAiRouteReq;
import com.GoWalk.domain.walk.application.data.response.WalkAiRouteRes;
import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import jakarta.annotation.PostConstruct;

@Slf4j
@Service
@RequiredArgsConstructor
public class WalkAiClient {

    @Resource(name = "walkAiRestTemplate")
    private final RestTemplate restTemplate;

    @Value("${ai.walk.base-url}")
    private String baseUrl;

    @Value("${ai.walk.route-path}")
    private String routePath;

    @PostConstruct
    void print() {
        log.info("[AI-CONFIG] baseUrl={}, routePath={}", baseUrl, routePath);
    }

    public WalkAiRouteRes recommendRoute(WalkAiRouteReq req) {
        String url = baseUrl + routePath;
        log.info("[AI-HTTP] --> POST {}", url);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        ResponseEntity<WalkAiRouteRes> response = restTemplate.exchange(
                url, HttpMethod.POST, new HttpEntity<>(req, headers), WalkAiRouteRes.class
        );
        return response.getBody();
    }
}
