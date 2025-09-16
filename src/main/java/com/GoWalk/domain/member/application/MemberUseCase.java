package com.GoWalk.domain.member.application;

import com.GoWalk.domain.member.application.data.MockMember;
import org.springframework.stereotype.Component;

@Component
public class MemberUseCase {
  public MockMember getMyInfo() {
    return new MockMember("gorani", "hello world");
  }
}

