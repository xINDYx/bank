package ru.yandex.front_ui.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import ru.yandex.bank.clients.accounts.AccountsClient;
import ru.yandex.bank.clients.cash.CashClient;
import ru.yandex.bank.clients.exchange.ExchangeClient;
import ru.yandex.bank.clients.transfer.TransferClient;

@Configuration
public class ClientsConfiguration {
    @Value("${clients.accounts.uri}")
    private String accountsUri;

    @Value("${clients.exchange.uri}")
    private String exchangeUri;

    @Value("${clients.cash.uri}")
    private String cashUri;

    @Value("${clients.transfer.uri}")
    private String transferUri;

    @Bean
    public WebClient webClient() {
        return WebClient.builder()
                .build();
    }

    @Bean
    public AccountsClient accountsClient(WebClient webClient) {
        return new AccountsClient(accountsUri, webClient);
    }

    @Bean
    public ExchangeClient exchangeClient(WebClient webClient) {
        return new ExchangeClient(exchangeUri, webClient);
    }

    @Bean
    public CashClient cacheClient(WebClient webClient) {
        return new CashClient(cashUri, webClient);
    }

    @Bean
    public TransferClient transferClient(WebClient webClient) {
        return new TransferClient(transferUri, webClient);
    }
}
