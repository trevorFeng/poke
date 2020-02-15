package com.poke.common.core;

import com.poke.common.bean.domain.mysql.User;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

public class UserContextHolder {

    private static String USER = "user";

    public static void set(User user) {
        RequestContextHolder.currentRequestAttributes().setAttribute(USER ,user ,RequestAttributes.SCOPE_REQUEST);
    }

    public static User currentUser() {
        return (User) RequestContextHolder.currentRequestAttributes().getAttribute(USER, RequestAttributes.SCOPE_REQUEST);
    }

}
