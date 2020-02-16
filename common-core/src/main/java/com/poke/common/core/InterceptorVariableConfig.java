package com.poke.common.core;

import com.poke.common.client.UserDbClient;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

@Configuration
public class InterceptorVariableConfig {

    @Resource
    public void setUserDbClient(UserDbClient userDbClient){
        UserConTextInterceptor.setUserDbClient(userDbClient);
    }
}
