package com.poke.zuul.feignInterceeeptor;

import com.poke.common.bean.bo.WebKeys;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.xml.ws.Service;
import java.util.Enumeration;

public class FeeignUserContextInterceptor implements RequestInterceptor {

    @Override
    public void apply(RequestTemplate requestTemplate) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        requestTemplate.header(WebKeys.TOKEN ,request.getHeader(WebKeys.TOKEN));
    }
}
