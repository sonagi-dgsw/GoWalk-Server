package com.GoWalk.domain.member.application;

import com.GoWalk.domain.member.application.data.MockMember;
import com.GoWalk.domain.member.application.data.res.GetMyInfoRes;
import com.GoWalk.domain.member.application.data.res.GetMyProfile;
import org.springframework.stereotype.Component;

@Component
public class MemberUseCase {
  public GetMyInfoRes getMyInfo() {
    return GetMyInfoRes.of(new MockMember("gorani", "legend"));
  }

  public GetMyProfile getMyProfile() {
    return GetMyProfile.of(new MockMember("고rani", "legend"));
  }
}

