package com.dailybrief.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Value("${python.api.base.url}")
    private String pythonApiBaseUrl;

    @Bean
    public WebClient pythonWebClient() {
        return WebClient.builder()
                .baseUrl(pythonApiBaseUrl)
                .filter(addJwtTokenHeader())
                .build();
    }

    private ExchangeFilterFunction addJwtTokenHeader() {
        return (request, next) -> {
            // TODO: Aqui, você deve obter o token JWT.
            // A lógica de como obter o token depende da sua implementação.
            // Por exemplo, você pode obtê-lo do contexto de segurança, de um serviço de
            // cache, ou de uma fonte externa.
            String jwtToken = "seu-token-jwt-aqui";

            if (jwtToken != null && !jwtToken.isEmpty()) {
                return next.exchange(
                        ClientRequest.from(request)
                                .header("Authorization", "Bearer " + jwtToken)
                                .build());
            }
            return next.exchange(request);
        };
    }
}