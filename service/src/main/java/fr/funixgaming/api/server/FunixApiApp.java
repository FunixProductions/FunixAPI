package fr.funixgaming.api.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication(scanBasePackages = "fr.funixgaming.api")
@EnableFeignClients(basePackages = "fr.funixgaming.api")
public class FunixApiApp {

    public static void main(final String[] args) {
        SpringApplication.run(FunixApiApp.class, args);
    }

}