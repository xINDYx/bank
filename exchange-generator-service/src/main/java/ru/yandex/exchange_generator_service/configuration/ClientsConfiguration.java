package ru.yandex.exchange_generator_service.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import ru.yandex.bank.clients.exchange.ExchangeClient;

@Configuration
public class ClientsConfiguration {
    @Value("${clients.exchange.uri}")
    private String exchangeUri;

    @Bean
    public WebClient webClient() {
        return WebClient.builder()
                .build();
    }

    @Bean
    public ExchangeClient exchangeClient() {
        return new ExchangeClient(exchangeUri, webClient());
    }
}
