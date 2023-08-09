package com.daesung.app.platform;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.scheduling.annotation.EnableAsync;

@Slf4j
@SpringBootApplication(scanBasePackages = "com.daesung.*")
@EnableAsync
public class PlatformApplication extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        log.info("configure called");
        return application.sources(PlatformApplication.class);
    }

    public static void main(String[] args) {
        log.info("configure called");
        SpringApplication.run(PlatformApplication.class, args);
    }
}
