package com.prac.deepseek;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.prac.deepseek.Mapper")
public class DeepseekApplication {

    public static void main(String[] args) {
        SpringApplication.run(DeepseekApplication.class, args);
    }

}
