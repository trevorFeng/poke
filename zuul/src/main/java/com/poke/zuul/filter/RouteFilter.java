package com.poke.zuul.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import com.poke.common.bean.bo.WebKeys;
import com.poke.common.util.TokenUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Slf4j
@Component
public class RouteFilter extends ZuulFilter {
    @Override
    public String filterType() {
        return  FilterConstants.PRE_TYPE;
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
        if (uri.startsWith("/auth")) {
            return false;
        }else {
            return true;
        }
    }

    @Override
    public Object run() throws ZuulException {
        RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletRequest request = ctx.getRequest();
        String token = request.getHeader(WebKeys.TOKEN);
        //解析token
        Map<String, Object> claims = TokenUtil.getClaimsFromToken(token);
        //String openid = (String) claims.get("openid");
        Integer userId = (Integer) claims.get("userid");
        //Long timestamp = (Long) claims.get("timestamp");

        //构造请求
        ctx.addZuulRequestHeader("userid" ,userId.toString());
        return true;
    }
}
