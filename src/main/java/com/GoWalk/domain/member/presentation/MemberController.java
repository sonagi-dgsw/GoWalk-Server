package com.GoWalk.domain.members.presentation;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/members")
public class MemberController {
  @GetMapping("/me")
  public String me() {
    return "Hello, Member!";
  }
}
