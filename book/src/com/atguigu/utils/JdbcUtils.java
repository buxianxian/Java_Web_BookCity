package com.atguigu.utils;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.DruidDataSourceFactory;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

public class JdbcUtils {

    private static DruidDataSource druidDataSource;
    private static ThreadLocal<Connection> conns = new ThreadLocal<>();

    static {
        try {
            Properties pro = new Properties();
            //读取jdbc.properties属性配置文件
            InputStream is = JdbcUtils.class.getClassLoader().getResourceAsStream("jdbc.properties");
            //从流中加载数据
            pro.load(is);
            //创建数据库连接池
            druidDataSource = (DruidDataSource) DruidDataSourceFactory.createDataSource(pro);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     *获取数据库连接池中的连接
     * @return 如果返回null，说明获取连接失败，有值就是获取连接成功
     */
    public static Connection getConnection() {
        Connection conn = conns.get();

        if (conn == null) {
            try {
                //从数据库连接池中获取连接
                conn = druidDataSource.getConnection();
                //设置为手动管理事务
                conn.setAutoCommit(false);
                //保存到ThreadLocal对象中，供后面的JDBC操作使用
                conns.set(conn);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return conn;
    }

    /**
     * 提交事务，并关闭释放连接
     */
    public static void commitAndClose() {
        Connection conn = conns.get();

        //如果不等于null，说明之前使用过连接，操作过数据库
        if (conn != null) {
            try {
                //提交事务
                conn.commit();
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                try {
                    //关闭连接，释放资源
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

        //一定要执行remove操作，否则就会出错（因为Tomcat服务器底层使用了线程池技术）
        conns.remove();
    }

    /**
     * 回滚事务，并关闭释放连接
     */
    public static void rollbackAndClose() {
        Connection conn = conns.get();

        //如果不等于null，说明之前使用过连接，操作过数据库
        if (conn != null) {
            try {
                //回滚事务
                conn.rollback();
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                try {
                    //关闭连接，释放资源
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

        //一定要执行remove操作，否则就会出错（因为Tomcat服务器底层使用了线程池技术）
        conns.remove();
    }

    /*
     * 关闭连接，放回数据库连接池

    public static void close(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
     */
}
