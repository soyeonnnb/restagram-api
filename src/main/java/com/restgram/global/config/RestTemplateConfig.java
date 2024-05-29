package com.restgram.global.config;

import com.restgram.domain.user.dto.request.CreateCalenderRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfig {
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public CreateCalenderRequest createCalenderRequest() {
        return CreateCalenderRequest.builder()
                .name("Restagram")
                .color("LAVENDER")
                .build();
    }
}
