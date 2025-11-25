package com.GoWalk.domain.member.application.data.res;

import com.GoWalk.domain.member.application.entity.Member;
import jakarta.validation.constraints.NotNull;

public record GetMyInfoRes(
    @NotNull String username,
    @NotNull String email,
    @NotNull String breed,
    @NotNull Integer breedAge,
    @NotNull Double petWeight

) {
  public static GetMyInfoRes of(Member member) {
    return new GetMyInfoRes(member.getUsername(), member.getEmail(), member.getBreed(), member.getBreedAge(), member.getPetWeight());
  }
}