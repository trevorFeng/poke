package com.poke.zuul.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import com.poke.common.bean.bo.WebKeys;
import com.poke.common.bean.domain.mysql.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import java.util.Objects;

@Slf4j
@Component
public class AccessTokenIsCorrectFilter extends ZuulFilter {

    @Override
    public String filterType() {
        return "pre";
    }

    @Override
    public int filterOrder() {
        return 0;
    }

    @Override
    public boolean shouldFilter() {
        RequestContext requestContext = RequestContext.getCurrentContext();
        HttpServletRequest request = requestContext.getRequest();
        String uri = request.getRequestURI();
        if (uri.startsWith("/pokeAuth")) {
            return false;
        }
        return true;
    }

    @Override
    public Object run() {
        RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletRequest request = ctx.getRequest();
        HttpServletResponse response = ctx.getResponse();
        String token = request.getHeader(WebKeys.TOKEN);

            //解析token
            Map<String, Object> claims = TokenUtil.getClaimsFromToken(token);
            String openid = (String) claims.get("openid");
            String hash = (String) claims.get("hash");
            Long timestamp = (Long) claims.get("timestamp");

            //三者必须存在,少一样说明token被篡改
            if (openid == null || hash == null || timestamp == null) {
                try {
                    response.sendRedirect(REDIRECT);
                } catch (IOException e) {
                    e.printStackTrace();
                    log.error("zuul login error ------>" + e);
                }
                return false;
            }
            //合法才通过
            User user = userService.findUserByOpenid(openid);
            if (user != null && Objects.equals(hash ,user.getHash())) {
                return Boolean.TRUE;
            }else {
                try {
                    response.sendRedirect(REDIRECT);
                } catch (IOException e) {
                    e.printStackTrace();
                    log.error("zuul login error ------>" + e);
                }
            }
        }
        return null;
    }
}
