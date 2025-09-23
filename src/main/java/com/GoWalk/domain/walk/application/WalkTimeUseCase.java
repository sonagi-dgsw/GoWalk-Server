package com.GoWalk.domain.walk.application;

import com.GoWalk.domain.walk.application.data.request.WalkTimeReq;
import com.GoWalk.domain.walk.application.data.response.WalkTimeRes;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Random;

@Component
@RequiredArgsConstructor
public class WalkTimeUseCase {

    private final Random random = new Random();

    public WalkTimeRes recommendTime(WalkTimeReq req) {
        List<String> times;

        if ("sunny".equalsIgnoreCase(req.weather())) {
            times = List.of("오전 6시", "오전 7시", "오후 5시");
        } else if ("rainy".equalsIgnoreCase(req.weather())) {
            return new WalkTimeRes("실내 활동 권장", "비가 오니 실내활동을 권장합니다.");
        } else {
            times = List.of("오전 6시", "오전 7시", "오후 5시", "오후 8시");
        }

        String chosenTime = times.get(random.nextInt(times.size()));

        String reason;
        if (chosenTime.contains("오전")) {
            reason = "시원한 아침이라 산책하기 좋습니다.";
        } else if (chosenTime.contains("오후 5시")) {
            reason = "적당한 온도로 산책하기 좋아요.";
        } else if (chosenTime.contains("오후 8시")) {
            reason = "저녁산책은 분위기가 좋아요";
        } else {
            reason = "만약 이 부분이 실행되었다면 개발자에게 커피를 사주십시오.";
        }

        return new WalkTimeRes(chosenTime, reason);
    }
}