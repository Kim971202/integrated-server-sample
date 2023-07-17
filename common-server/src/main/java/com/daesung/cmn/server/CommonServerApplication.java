package com.daesung.cmn.server;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;

@Slf4j
@SpringBootApplication(scanBasePackages = {"com.daesung.*"})
public class CommonServerApplication {

    public static void main(String[] args) throws IOException {
        log.info("CommonServerApplication called");
        SpringApplication.run(CommonServerApplication.class, args);
    }
}
