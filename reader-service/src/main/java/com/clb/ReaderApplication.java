package com.clb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.transaction.annotation.EnableTransactionManagement;

//@EnableCaching
@EnableFeignClients
@SpringBootApplication
@EnableTransactionManagement
public class ReaderApplication {
    public static void main(String[] args) {
        SpringApplication.run(ReaderApplication.class, args);
    }
}
