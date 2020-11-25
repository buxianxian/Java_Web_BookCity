package com.atguigu.test;

import java.lang.reflect.Method;
import java.util.HashMap;

public class UserServletTest {
    public void login() {
        System.out.println("login方法调用了");
    }

    public void regist() {
        System.out.println("regist方法调用了");
    }

    public static void main(String[] args) {
        try {
            Method method = UserServletTest.class.getDeclaredMethod("login");
            System.out.println(method);
            method.invoke(new UserServletTest());

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
