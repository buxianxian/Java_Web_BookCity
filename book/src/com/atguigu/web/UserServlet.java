package com.atguigu.web;

import com.atguigu.pojo.User;
import com.atguigu.service.UserService;
import com.atguigu.service.impl.UserServiceImpl;
import com.atguigu.utils.WebUtils;
import com.google.gson.Gson;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static com.google.code.kaptcha.Constants.KAPTCHA_SESSION_KEY;

public class UserServlet extends BaseServlet {

    private UserService userService = new UserServiceImpl();

    protected void ajaxExistsUsername(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        //获取请求的参数username
        String username = request.getParameter("username");
        //调用userService.existsUsername();
        Boolean existUsername = userService.existsUsername(username);
        //把返回的结果封装成为Map对象
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("existUsername", existUsername);

        Gson gson = new Gson();
        String json = gson.toJson(resultMap);

        response.getWriter().write(json);
    }

    //处理登录的功能
    protected void login(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        //1、获取请求的参数
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        // 调用 userService.login()登录处理业务
        User userLogin = userService.login(new User(null, username, password, null));

        // 如果等于null,说明登录失败!
        if (userLogin == null) {
            //把错误信息，和回显的表单项信息，保存到request域中
            request.setAttribute("msg", "用户名或密码错误");
            request.setAttribute("username", username);

            // 跳回登录页面
            request.getRequestDispatcher("/pages/user/login.jsp").forward(request, response);
        } else {
            // 登录 成功
            //保存用户登录的信息到session域中
            request.getSession().setAttribute("user", userLogin);
            // 跳到成功页面 login_success.jsp
            request.getRequestDispatcher("/pages/user/login_success.jsp").forward(request, response);
        }
    }

    //处理注销的功能
    protected void logout(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        //1、销毁session中用户登录的信息（或者销毁session）
        request.getSession().invalidate();;
        //2、重定向到首页（或登录页面）
        response.sendRedirect(request.getContextPath());
    }

    //处理注册的功能
    protected void regist(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        //获取session中的验证码
        String token = (String) request.getSession().getAttribute(KAPTCHA_SESSION_KEY);
        //删除Session中的验证码
        request.getSession().removeAttribute(KAPTCHA_SESSION_KEY);

        //1、获取请求的参数
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String email = request.getParameter("email");
        String code = request.getParameter("code");

        /*
        Map<String, String[]> parameterMap = request.getParameterMap();
        for(Map.Entry<String, String[]> entry: parameterMap.entrySet()) {
            System.out.println(entry.getKey() + " = " + Arrays.asList(entry.getValue()));
        }
         */

        //代码可以更加简洁，这里传的是User对象，返回的是User对象的引用，那么传的是其他对象，返回的是其他对象的引用
        //所以可以使用泛型
        User user = WebUtils.copyParamToBean(request.getParameterMap(), new User());

        //2、检查验证码是否正确
        if (token != null && token.equalsIgnoreCase(code)) { //验证码忽略大小写
            //3、检查用户名是否可用
            if (userService.existsUsername(username)) {
                System.out.println("用户名["+username+"]已存在");

                //把回显信息，保存到request域中
                request.setAttribute("msg", "用户名已存在");
                request.setAttribute("username", username);
                request.setAttribute("email", email);

                //跳回注册页面
                request.getRequestDispatcher("/pages/user/regist.jsp").forward(request, response);

            } else {
                //可用 调用service保存到数据库
                userService.registUser(new User(null, username, password, email));
                //跳转到注册成功页面
                request.getRequestDispatcher("/pages/user/regist_success.jsp").forward(request, response);
            }
        } else {

            //把回显信息，保存到request域中
            request.setAttribute("msg", "验证码错误");
            request.setAttribute("username", username);
            request.setAttribute("email", email);

            System.out.println("验证码["+code+"]错误");
            request.getRequestDispatcher("/pages/user/regist.jsp").forward(request, response);
        }
    }

}
