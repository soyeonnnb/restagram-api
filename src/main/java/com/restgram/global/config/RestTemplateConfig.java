package com.restgram.global.config;

import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfig {

    static final int READ_TIMEOUT = 1500;
    static final int CONN_TIMEOUT = 3000;

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
