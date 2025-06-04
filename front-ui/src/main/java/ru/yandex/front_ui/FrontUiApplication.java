package ru.yandex.front_ui;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@Slf4j
@EnableDiscoveryClient
@SpringBootApplication
public class FrontUiApplication {

    public static void main(String[] args) {
        SpringApplication.run(FrontUiApplication.class, args);

        log.info("FrontUiApplication started");
    }
}
