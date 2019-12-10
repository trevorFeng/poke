package com.poke.common.client;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient("db-service")
public interface cardConsumDbClient {
}
