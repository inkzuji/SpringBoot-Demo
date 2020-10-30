package com.zuji;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;

@Slf4j
@SpringBootApplication
// @EnableWebMvc
public class ZujiApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext application = SpringApplication.run(ZujiApplication.class, args);
        Environment env = application.getEnvironment();
        String active = env.getProperty("spring.profiles.active");
        String port = env.getProperty("server.port");
        String path = env.getProperty("server.servlet.context-path");
        log.info("\n----------------------------" + active + "------------------------------\n\t" +
                "Application is running!  Access URLs:\n\t" +
                "Local: \t\thttp://localhost:" + port + path + "/\n\t" +
                "swagger-ui: \t\thttp://localhost:" + port + path + "/doc.html#/plus\n" +
                "----------------------------------------------------------");
    }
}