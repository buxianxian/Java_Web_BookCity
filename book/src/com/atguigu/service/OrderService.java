package com.atguigu.service;

import com.atguigu.pojo.Cart;

public interface OrderService {
    String createOrder(Cart cart, Integer userId);
}
