package com.atguigu.filter;

import com.atguigu.utils.JdbcUtils;

import javax.servlet.*;
import java.io.IOException;

public class TransactionFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        try {
            filterChain.doFilter(servletRequest, servletResponse);
            //提交事务
            JdbcUtils.commitAndClose();
        } catch (Exception e) {
            //回滚事务
            JdbcUtils.rollbackAndClose();
            e.printStackTrace();
            //把异常抛给Tomcat服务器
            throw new RuntimeException(e);
        }
    }

    @Override
    public void destroy() {

    }
}
