package com.yang.blog;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan({
        "com.github.yhl452493373.config",
        "com.yang.blog.es.service",
        "com.yang.blog.config",
        "com.yang.blog.mapper",
        "com.yang.blog.service",
        "com.yang.blog.controller"})
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
