package com.poke.common.core;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * @Auther: trevor
 * @Date: 2019\3\28 0028 01:03
 * @Description:
 */
@Configuration
@EnableWebMvc
public class InterceptorConfigurer extends WebMvcConfigurerAdapter {

    @Bean
    public UserConTextInterceptor getUserConTextInterceptor(){
        return new UserConTextInterceptor();
    }


    /**
     * 请求拦截器，在进入应用时解析token
     * @param registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new UserConTextInterceptor()).addPathPatterns("/**");
        super.addInterceptors(registry);
    }

//    /**
//     * 创建Feign请求拦截器，在发送请求前设置header的token
//     * @return
//     */
//    @Bean
//    @ConditionalOnClass(Feign.class)
//    public FeignUserContextInterceptor feignTokenInterceptor(){
//        return new FeignUserContextInterceptor();
//    }





}
