package ru.yandex.exchange_generator_service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
@Slf4j
public class ExchangeGeneratorApplication {

	public static void main(String[] args) {
		SpringApplication.run(ExchangeGeneratorApplication.class, args);

		log.info("ExchangeGeneratorApplication started");
	}
}
