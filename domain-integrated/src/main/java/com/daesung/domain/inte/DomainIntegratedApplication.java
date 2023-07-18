package com.daesung.domain.inte;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.daesung.*")
public class DomainIntegratedApplication {

    public static void main(String[] args) throws Exception {
        SpringApplication.run(DomainIntegratedApplication.class, args);
    }

}
