package com.poke.zuul.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.poke.common.bean.bo.ResponseHelper;
import com.poke.common.bean.bo.WebKeys;
import com.poke.common.bean.domain.mysql.User;
import com.poke.common.bean.enums.MessageCodeEnum;
import com.poke.common.client.UserServiceClient;
import com.poke.common.util.JsonUtil;
import com.poke.common.util.TokenUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.Objects;

@Slf4j
@Component
public class AccessFilter extends ZuulFilter {

    @Resource
    private UserServiceClient userServiceClient;

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
        }else {
            //解析token
            Map<String, Object> claims = TokenUtil.getClaimsFromToken(token);
            String openid = (String) claims.get("openid");
            String hash = (String) claims.get("hash");
            Long timestamp = (Long) claims.get("timestamp");

            //三者必须存在,少一样说明token被篡改
            if (openid == null || hash == null || timestamp == null) {
                ctx.setSendZuulResponse(false);
                ctx.setResponseStatusCode(401);
                ctx.setResponseBody(JsonUtil.toJsonString(ResponseHelper.createInstanceWithOutData(MessageCodeEnum.TOKEN_ERROR)));
                return null;
            }
            //合法才通过
            User user = userServiceClient.findByOpenid(openid).getData();
            if (user != null && Objects.equals(hash ,user.getHash())) {
                return Boolean.TRUE;
            }else {
                ctx.setSendZuulResponse(false);
                ctx.setResponseStatusCode(401);
                ctx.setResponseBody(JsonUtil.toJsonString(ResponseHelper.createInstanceWithOutData(MessageCodeEnum.TOKEN_ERROR)));
                return null;
            }
        }
    }
}
