/*
package com.weixin.filter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import java.io.IOException;

*/
/**
 * Created by Administrator on 2018/3/8.
 *//*

@WebFilter(urlPatterns = "*/
/*", filterName = "codeFilter")
public class CodeFilter implements Filter {

    @Override
    public void destroy() {
        System.out.println("filter2 destroy method");
    }
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain arg2)
            throws IOException, ServletException {
        response.setContentType("text/html;charset=UTF-8");
        response.setContentType("text/plain;charset=UTF-8");
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        arg2.doFilter(request,response);
        System.out.println("filter2 doFilter method");
    }
    @Override
    public void init(FilterConfig arg0) throws ServletException {
        System.out.println("filter2 init method");
    }

}
*/
