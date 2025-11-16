package com.GoWalk.global.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
public class EmailConfig {
	@Value("${spring.mail.username}") private String username;
	@Value("${spring.mail.password}") private String password;

	@Bean
	public JavaMailSender mailSender() {
		JavaMailSenderImpl mailSender = new JavaMailSenderImpl(); // 메일 보낼 때 사용되는 구현체

		mailSender.setHost("smtp.gmail.com"); // 메일 보낼 때 Gmail SMTP 서버 사용
		mailSender.setPort(587); // TLS 포트 넘버
		mailSender.setUsername(username);
		mailSender.setPassword(password);

		Properties mailProperties = new Properties();
		mailProperties.put("mail.transport.protocol", "smtp"); // SMTP를 메인 프로토콜로 지정
		mailProperties.put("mail.smtp.auth", "true"); // SMTP 로그인 시 인증 요구 활성화
		mailProperties.put("mail.smtp.starttls.enable", "true"); // 평문연결을 TLS 연결로 변경
		mailProperties.put("mail.debug", "true"); // 디버그 모드 활성화
		mailProperties.put("mail.smtp.ssl.trust", "smtp.gmail.com"); // stmp.gmail.com를 신뢰하는 호스트로 지정
		mailProperties.put("mail.smtp.ssl.protocols", "TLSv1.3"); // TLS v1.3 사용

		mailSender.setJavaMailProperties(mailProperties);
		return mailSender;
	}
}
