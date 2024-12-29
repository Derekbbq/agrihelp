package com.agrihelp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@MapperScan("com.agrihelp.mapper")
public class AgriHelpApplication {
    public static void main(String[] args) {
        SpringApplication.run(AgriHelpApplication.class, args);
    }
}
