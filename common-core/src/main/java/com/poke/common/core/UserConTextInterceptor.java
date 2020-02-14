package com.poke.common.core;

import com.poke.common.bean.domain.mysql.User;
import com.poke.common.client.UserDbClient;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class UserConTextInterceptor extends HandlerInterceptorAdapter {

    @Resource
    private UserDbClient userDbClient;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (request.getMethod().equals("OPTIONS")) {
            return true;
        }
        String userid = request.getHeader("userid");
        User user = userDbClient.findByUserId(Integer.valueOf(userid)).getData();
        UserContextHolder.set(user);
        return Boolean.TRUE;
    }
}
