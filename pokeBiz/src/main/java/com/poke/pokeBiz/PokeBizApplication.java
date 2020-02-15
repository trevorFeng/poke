package com.poke.pokeBiz;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication(scanBasePackages = {"com.poke.pokeBiz","com.poke.common.client","com.poke.common.core"})
@EnableEurekaClient
@EnableFeignClients(basePackages = "com.poke.common.client")
public class PokeBizApplication {

    public static void main(String[] args) {
        SpringApplication.run(PokeBizApplication.class, args);
    }

}
