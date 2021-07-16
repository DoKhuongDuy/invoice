package com.viettel.bccs3;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class JobInvoiceServiceMain {

    public static void main(String[] args) {
        SpringApplication.run(JobInvoiceServiceMain.class, args);
    }
}
