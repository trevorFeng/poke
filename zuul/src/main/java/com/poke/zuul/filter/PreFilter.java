package com.poke.zuul.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.poke.common.bean.bo.ResponseHelper;
import com.poke.common.bean.bo.WebKeys;
import com.poke.common.bean.enums.MessageCodeEnum;
import com.poke.common.util.JsonUtil;
import com.poke.zuul.service.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class PreFilter extends ZuulFilter {

    @Resource
    private RedisService redisService;

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
        }else {
            return true;
        }
    }

    @Override
    public Object run() {
        RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletRequest request = ctx.getRequest();
        String token = request.getHeader(WebKeys.TOKEN);
        if (token == null) {
            ctx.setSendZuulResponse(false);
            ctx.setResponseStatusCode(401);
            ctx.setResponseBody(JsonUtil.toJsonString(ResponseHelper.createInstanceWithOutData(MessageCodeEnum.TOKEN_ERROR)));
            return null;
        }
        //如果token存在于redis的key中，则更新过期时间，默认7天
        if (redisService.hasKey("acess_token:" + token)) {
            redisService.setValueWithExpire("acess_token:" + token ,String.valueOf(System.currentTimeMillis())
                    ,7 * 24 * 60 * 60L , TimeUnit.MILLISECONDS);
        }else {
            ctx.setSendZuulResponse(false);
            ctx.setResponseStatusCode(401);
            ctx.setResponseBody(JsonUtil.toJsonString(ResponseHelper.createInstanceWithOutData(MessageCodeEnum.TOKEN_ERROR)));
            return null;
        }
        return true;
    }
}
