package com.GoWalk.domain.walk.presentation;

import com.GoWalk.domain.walk.application.data.req.WalkEndReq;
import com.GoWalk.domain.walk.application.data.req.WalkQuitReq;
import com.GoWalk.domain.walk.application.data.req.WalkStartReq;
import com.GoWalk.domain.walk.application.data.res.*;
import com.GoWalk.domain.walk.application.usecase.WalkUseCase;
import com.GoWalk.global.data.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/walk")
@RequiredArgsConstructor
@Slf4j
public class WalkController {
    private final WalkUseCase walkUseCase;

    // 산책 시작
    @PostMapping("/start")
    public ApiResponse<WalkStartRes> walkStart(@Valid @RequestBody WalkStartReq request) {
        return walkUseCase.walkStart(request);
    }

    // 산책완료
    @PostMapping("/finish")
    public ApiResponse<WalkEndRes> walkEnd(@Valid @RequestBody WalkEndReq request) {
        return walkUseCase.walkEnd(request);
    }

    // 산책중도 포기
    @DeleteMapping("/quit")
    public ApiResponse<WalkQuitRes> walkQuit(@Valid @RequestBody WalkQuitReq request) {
        return walkUseCase.walkQuit(request);
    }
}
