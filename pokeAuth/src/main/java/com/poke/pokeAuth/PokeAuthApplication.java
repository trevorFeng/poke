package com.poke.pokeAuth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication(scanBasePackages = {"com.poke.pokeAuth","com.poke.common.client"})
@EnableEurekaClient
@EnableFeignClients
public class PokeAuthApplication {

    public static void main(String[] args) {
        SpringApplication.run(PokeAuthApplication.class, args);
    }

}
