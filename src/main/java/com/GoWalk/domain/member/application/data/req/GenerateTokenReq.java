package com.GoWalk.domain.member.application.data.req;

import com.GoWalk.domain.member.application.entity.Role;
import jakarta.validation.constraints.NotNull;

public record GenerateTokenReq(
		@NotNull String username,
		Role role
){
}
