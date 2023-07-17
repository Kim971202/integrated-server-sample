package com.daesung.app.dggw;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication(scanBasePackages = "com.daesung.*")
@EnableAsync
@Slf4j
public class DggwApplication extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        log.info("SpringApplicationBuilder Called");
        return application.sources(DggwApplication.class);
    }

    public static void main(String[] args) {
        log.info("MAIN Application Called");
        SpringApplication.run(DggwApplication.class, args);
    }
}

