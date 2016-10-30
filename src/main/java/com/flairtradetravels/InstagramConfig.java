package com.flairtradetravels;

import org.jinstagram.auth.InstagramAuthService;
import org.jinstagram.auth.oauth.InstagramService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class InstagramConfig {

    @Value("${instagram.clientId}")
    private String clientId;
    @Value("${instagram.clientSecret}")
    private String clientSecret;
    @Value("${instagram.callbackUrl}")
    private String callbackUrl;


    @Bean InstagramService instagramService() {
        return new InstagramAuthService()
            .apiKey(clientId)
            .apiSecret(clientSecret)
            .callback(callbackUrl)
            .build();
    }
}
