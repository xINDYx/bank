package ru.yandex.blocker_service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
@Slf4j
public class BlockerServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(BlockerServiceApplication.class, args);

		log.info("BlockerServiceApplication started");
	}

}
