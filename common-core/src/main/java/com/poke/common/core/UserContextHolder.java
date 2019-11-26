package com.poke.common.core;

import com.poke.common.bean.domain.mysql.User;

public class UserContextHolder {
    private static ThreadLocal<User> context = new ThreadLocal<>();

    public static User currentUser() {
        return context.get();
    }

    public static void set(User user) {
        context.set(user);
    }

    public static void shutDown(){
        context.remove();
    }
}
