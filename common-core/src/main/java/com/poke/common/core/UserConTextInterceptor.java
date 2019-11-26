package com.poke.common.core;

import com.poke.common.bean.bo.WebKeys;
import com.poke.common.bean.domain.mysql.User;
import com.poke.common.client.UserServiceClient;
import com.poke.common.util.TokenUtil;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

public class UserConTextInterceptor extends HandlerInterceptorAdapter {

    private UserServiceClient userServiceClient;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (request.getMethod().equals("OPTIONS")) {
            return true;
        }
        String token = request.getHeader(WebKeys.TOKEN);
        Map<String, Object> claims = TokenUtil.getClaimsFromToken(token);
        String openid = (String) claims.get("openid");
        User user = userServiceClient.findByOpenid(openid).getData();
        UserContextHolder.set(user);
        return Boolean.TRUE;
    }
}
