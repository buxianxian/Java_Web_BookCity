package com.atguigu.web;

import com.atguigu.pojo.Cart;
import com.atguigu.pojo.User;
import com.atguigu.service.OrderService;
import com.atguigu.service.impl.OrderServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class OrderServlet extends BaseServlet {

    private OrderService orderService = new OrderServiceImpl();

    //生成订单
    protected void createOrder(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //先获取Cart购物车对象
        Cart cart = (Cart) request.getSession().getAttribute("cart");

        //获取userId
        User loginUser = (User) request.getSession().getAttribute("user");

        if (loginUser == null) {
            //结账之前一定要登录，如果没有登录，就直接跳转到登录页面
            request.getRequestDispatcher("/pages/user/login.jsp").forward(request, response);
            //重定向或者转发之后的代码如果不需要执行了，必须使用return提前返回
            return;
        }

        Integer userID = loginUser.getId();

        //调用orderService.createOrder(cart, userID); 生成订单
        String orderID = orderService.createOrder(cart, userID);

        //request.setAttribute("orderId", orderID);

        //请求转发到/pages/cart/checkout.jsp
        //request.getRequestDispatcher("/pages/cart/checkout.jsp").forward(request, response);

        request.getSession().setAttribute("orderId", orderID);

        response.sendRedirect(request.getContextPath() + "/pages/cart/checkout.jsp");
    }
}
