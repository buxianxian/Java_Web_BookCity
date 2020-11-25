package com.atguigu.test;

import com.atguigu.dao.OrderDao;
import com.atguigu.dao.impl.OrderDaoImpl;
import com.atguigu.pojo.Order;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Date;

public class OrderDaoTest {

    @Test
    public void saveOrder() {
        OrderDao orderDao = new OrderDaoImpl();
        //这里的userId一定是要参照数据库中的用户表，不能随意乱写一个userId
        orderDao.saveOrder(new Order("1234567890",new Date(),new BigDecimal(100),0, 1));
    }
}