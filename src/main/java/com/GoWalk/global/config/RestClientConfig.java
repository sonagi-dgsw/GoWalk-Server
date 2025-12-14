package com.GoWalk.global.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

@Configuration
public class RestClientConfig {

    @Bean
    public RestTemplate restTemplate() {
        SimpleClientHttpRequestFactory f = new SimpleClientHttpRequestFactory();
        f.setConnectTimeout((int) Duration.ofSeconds(3).toMillis());
        f.setReadTimeout((int) Duration.ofSeconds(5).toMillis());
        return new RestTemplate(f);
    }

    @Bean(name = "walkAiRestTemplate")
    public RestTemplate walkAiRestTemplate() {
        SimpleClientHttpRequestFactory f = new SimpleClientHttpRequestFactory();
        f.setConnectTimeout((int) Duration.ofSeconds(5).toMillis());
        f.setReadTimeout((int) Duration.ofSeconds(120).toMillis());

        RestTemplate rt = new RestTemplate(new BufferingClientHttpRequestFactory(f));
        rt.setInterceptors(List.of(loggingInterceptor()));
        return rt;
    }

    private ClientHttpRequestInterceptor loggingInterceptor() {
        return (request, body, execution) -> {
            long start = System.currentTimeMillis();
            System.out.println("[AI-HTTP] --> " + request.getMethod() + " " + request.getURI());
            System.out.println("[AI-HTTP] requestBody=" + new String(body, StandardCharsets.UTF_8));

            var response = execution.execute(request, body);

            String respBody;
            try (var br = new BufferedReader(new InputStreamReader(response.getBody(), StandardCharsets.UTF_8))) {
                respBody = br.lines().collect(Collectors.joining("\n"));
            }
            long took = System.currentTimeMillis() - start;

            System.out.println("[AI-HTTP] <-- " + response.getStatusCode() + " (" + took + "ms)");
            System.out.println("[AI-HTTP] responseBody=" + respBody);
            return response;
        };
    }
}
