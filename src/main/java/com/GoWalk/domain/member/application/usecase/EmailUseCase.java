package com.GoWalk.domain.member.application.usecase;

import com.GoWalk.domain.member.application.data.req.EmailVerifyReq;
import com.GoWalk.domain.member.application.data.res.EmailRes;
import com.GoWalk.domain.member.application.exception.MemberException;
import com.GoWalk.domain.member.application.exception.MemberStatusCode;
import com.GoWalk.global.config.RedisConfig;
import com.GoWalk.global.data.ApiResponse;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class EmailUseCase {
	private final JavaMailSender mailSender;
	private final RedisConfig redisConfig;

	@Value("${spring.mail.username}") private String serviceName;
	// 인증번호 생성

	public int makeRandomNum() {
		return 100000 + new Random().nextInt(899999); // 6자리 인증번호
	}

	// 이메일 전송
	public void sendEmail(String email) {
		int authNum = makeRandomNum();

		makeRandomNum();
		String title = "산책가자 회원가입용 본인인증 코드입니다";
		String message = """
				<p>안녕하세요, 산책가자 회원가입을 위한 인증 코드입니다.</p>
				            <p>인증코드는 다음과 같습니다.</p>
				            <p><b>{{AUTH_CODE}}</b></p>
				            <p>이 코드를 입력하여 인증을 완료해주세요.</p>
			""";

		String content = message.replace("{{AUTH_CODE}}", String.valueOf(authNum));
		MimeMessage sendMessage = mailSender.createMimeMessage();
		try {
			MimeMessageHelper helper = new MimeMessageHelper(sendMessage, true, "utf-8");
			helper.setFrom(serviceName);
			helper.setTo(email);
			helper.setSubject(title);
			helper.setText(content, true);
			mailSender.send(sendMessage);
		} catch (MessagingException e) {
			throw new MemberException(MemberStatusCode.MEMBER_CANNOT_FOUND);
		}
		// Redis에 5분간 저장
		ValueOperations<String, String> valueOperations = redisConfig.redisTemplate().opsForValue();
		valueOperations.set(email, Integer.toString(authNum), 3, TimeUnit.MINUTES);
	}

	// 이메일 인증 확인
	public ApiResponse<EmailRes> verifyEmail(EmailVerifyReq request) {
		ValueOperations<String, String> valueOperations = redisConfig.redisTemplate().opsForValue();
		String code =  valueOperations.get(request.email());

		if (Objects.equals(code, request.authNum())) {
			redisConfig.redisTemplate().delete(request.email());
			redisConfig.redisTemplate().delete(request.authNum());
			return ApiResponse.ok(new EmailRes("이메일이 인증되었습니다."));
		} else {
			throw new MemberException(MemberStatusCode.EMAIL_CANNOT_SEND, "이메일 인증에 실패했습니다.");
		}
	}
}
