package com.atguigu.utils;

import org.apache.commons.beanutils.BeanUtils;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;

public class WebUtils {

    /*
    public static void comParamToBean(HttpServletRequest request, Object bean) {
        System.out.println("注入之前" +  bean);
        //把所有请求的参数都注入到bean对象当中
        try {
            BeanUtils.populate(bean, request.getParameterMap());
            System.out.println("注入之后" + bean);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }
     */

    /**
     *为什么我们不使用HttpServletRequest request做参数了呢？因为使用一个Map，把Map集合中的值注入到JavaBean中
     * 是很常见的操作。如果我们使用了request做参数，那Dao层和service层是使用不了这个参数的，而如果写成Map value
     * 在三层里都可以使用，如果写成request，那么将导致web层耦合度高
     * 作用：把Map中的值注入到对应的JavaBean属性中
     * @param value
     * @param bean
     */
    public static <T> T copyParamToBean(Map value, T bean) {
        System.out.println("注入之前" +  bean);
        //把所有请求的参数都注入到bean对象当中
        try {
            BeanUtils.populate(bean, value);
            System.out.println("注入之后" + bean);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

        return bean;
    }

    /**
     * 将字符串转换成为int类型的数据
     * @param strInt 字符串
     * @param defaultValue 默认值
     * @return
     */
    public static int parseInt(String strInt, int defaultValue) {
        //该异常是运行时异常，不是编译时异常
        try {
            return Integer.parseInt(strInt);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return defaultValue;
    }
}
