package com.atguigu.web;

import com.atguigu.pojo.Book;
import com.atguigu.pojo.Page;
import com.atguigu.service.BookService;
import com.atguigu.service.impl.BookServiceImpl;
import com.atguigu.utils.WebUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class BookServlet extends BaseServlet{

    private BookService bookService = new BookServiceImpl();

    //处理分页功能
    protected void page(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        //1、获取请求的参数pageNo，pageSize
        int pageNo = WebUtils.parseInt(request.getParameter("pageNo"), 1);
        int pageSize = WebUtils.parseInt(request.getParameter("pageSize"), Page.PAGE_SIZE);

        //2、调用BookService.page(pageNo,pageSize):page对象
        Page<Book> page = bookService.page(pageNo, pageSize);

        page.setUrl("manager/bookServlet?action=page");

        //3、保存Page对象到Request域中
        request.setAttribute("page", page);

        //4、请求转发到pages/manager/book_manager.jsp页面
        request.getRequestDispatcher("/pages/manager/book_manager.jsp").forward(request, response);
    }

    protected void add(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int pageNo = WebUtils.parseInt(request.getParameter("pageNo"), 0);
        pageNo++;

        //1、获取请求的参数，封装称为Book对象
        Book book = WebUtils.copyParamToBean(request.getParameterMap(), new Book());

        //2、调用BookService.addBook()保存图书
        bookService.addBook(book);

        //3、跳到图书列表页面 /manager/bookServlet?action=list
        //注意：这里不能使用转发，否则当用户刷新页面的时候会重复向数据库中添加数据
        //当用户提交完请求，浏览器会记录下最后一次请求的全部信息。当用户按下功能键F5，就会发起浏览器记录的最后一次请求
        //所以必须要使用重定向
        response.sendRedirect("/book/manager/bookServlet?action=page&pageNo=" + pageNo);

    }

    protected void delete(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        //1、获取请求的参数id，图书编号
        int id = WebUtils.parseInt(request.getParameter("id"), 0);

        //2、调用bookService.deleteBookById(); 删除图书
        bookService.deleteBookById(id);

        //3、重定向回图书列表管理页面 /book/manager/bookServlet?action=list
        response.sendRedirect(request.getContextPath() + "/manager/bookServlet?action=page&pageNo=" + request.getParameter("pageNo"));
    }

    protected void update(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        //1、获取请求参数，封装称为Book对象
        Book book = WebUtils.copyParamToBean(request.getParameterMap(), new Book());

        //2、调用BookService.updateBook(book)修改图书
        bookService.updateBook(book);

        //3、重定向回图书列表管理页面
        response.sendRedirect(request.getContextPath() + "/manager/bookServlet?action=page&pageNo=" + request.getParameter("pageNo"));

    }

    protected void getBook(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        //1、获取请求的参数 图书编号
        int id = WebUtils.parseInt(request.getParameter("id"), 0);

        //2、调用bookService.queryBookById()查询图书
        Book book = bookService.queryBookById(id);

        //3、保存图书到request域中
        request.setAttribute("book", book);

        //4、请求转发到 pages/manager/book_edit.jsp页面
        request.getRequestDispatcher("/pages/manager/book_edit.jsp").forward(request, response);

    }

    protected void list(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        //1、通过BookService查询全部图书
        List<Book> books = bookService.queryBooks();

        //2、把全部图书保存到Request域当中
        request.setAttribute("books", books);

        //3、请求转发到/pages/manager/book_manager.jsp
        request.getRequestDispatcher("/pages/manager/book_manager.jsp").forward(request, response);

    }
}
