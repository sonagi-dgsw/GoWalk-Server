package com.GoWalk.domain.member.application.data.req;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record EmailSendReq (
	@NotBlank @Email String email
) {}
