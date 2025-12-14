package com.GoWalk.domain.walk.application;

import com.GoWalk.domain.walk.application.data.req.WalkTimeReq;
import com.GoWalk.domain.walk.application.data.res.WalkTimeRes;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class WalkTimeUseCase {

    private final RestTemplate restTemplate;

    private static final String AI_SERVER_URL = "http://ai-server:8081/api/ai/walk-time";

    public WalkTimeRes recommendTime(WalkTimeReq req) {
        WalkTimeRes response = restTemplate.postForObject(
                AI_SERVER_URL,
                req,
                WalkTimeRes.class
        );

        if (response == null) {
            throw new IllegalStateException("AI 서버로부터 유효한 응답을 받지 못했습니다.");
        }

        return response;
    }
}
