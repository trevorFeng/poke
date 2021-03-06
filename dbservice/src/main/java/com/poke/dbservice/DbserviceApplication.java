package com.poke.dbservice;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableEurekaClient
@EnableFeignClients
@MapperScan("com.poke.dbservice.dao.mysql")
@EnableMongoRepositories(basePackages = {"com.poke.dbservice.dao.mongo"})
public class DbserviceApplication {

    public static void main(String[] args) {
        SpringApplication.run(DbserviceApplication.class, args);
    }

}
