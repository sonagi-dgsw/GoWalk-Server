package com.GoWalk.domain.walk.application.data.request;

public record WalkTimeReq(
        String petType,
        String lastWalk,
        String weather
) {}
