package com.daesung.domain.jpa;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@SpringBootApplication(scanBasePackages = "com.daesung.*")
public class DomainServiceJpaApplication {
    public static void main(String[] args) throws Exception {
        log.info("DomainServiceJpaApplication main called");
        SpringApplication.run(DomainServiceJpaApplication.class, args);
    }
}
